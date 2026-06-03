# Report – Guess The Number (Go)

---

## Indice

1. [Analisi del problema](#1-analisi-del-problema)
2. [Architettura proposta](#2-architettura-proposta)
3. [Gestione della concorrenza](#3-gestione-della-concorrenza)
4. [Sviluppo](#4-sviluppo)
5. [Risultati e considerazioni](#5-risultati-e-considerazioni)

---

## 1. Analisi del problema

Un **Oracolo** estrae un numero pseudocasuale in `[0, MAX]` e `N` giocatori tentano di indovinarlo. Le regole:

- A ogni turno ogni giocatore invia **esattamente un tentativo**.
- L'Oracolo notifica tutti contemporaneamente; l'ordine di arrivo è **non deterministico**.
- Se un tentativo è corretto: vittoria al vincitore, sconfitta agli altri — il gioco termina.
- Altrimenti: hint (troppo grande / troppo piccolo) e nuovo turno.
- Ogni giocatore può disattivare il bot e giocare manualmente.


| Requisito | Descrizione                                      |
| --------- | ------------------------------------------------ |
| **R1**    | N giocatori concorrenti                          |
| **R2**    | Ordine non deterministico per turno              |
| **R3**    | Un solo tentativo per giocatore per turno        |
| **R4**    | L'Oracolo scandisce i turni                      |
| **R5**    | Terminazione pulita senza panic su canali chiusi |

---

## 2. Architettura proposta

Il sistema usa due entità — **Oracle** e **Player** — che comunicano solo tramite **channel** Go, senza lock o mutex, seguendo il principio _"Do not communicate by sharing memory; share memory by communicating"_.

### 2.1 Architettura delle goroutine

```mermaid
graph TD
    MAIN(["[main]<br/>Main"])
    ORACLE(["[goroutine]<br/>Oracle"])
    P0(["[goroutine]<br/>Player 0"])
    P1(["[goroutine]<br/>Player 1"])
    PN(["[goroutine]<br/>Player N"])

    MAIN -->|"go ReceiveTries"| ORACLE
    MAIN -->|"go ReceiveWeakUp<br/>go ReceiveAnswer"| P0
    MAIN -->|"go ReceiveWeakUp<br/>go ReceiveAnswer"| P1
    MAIN -->|"go ReceiveWeakUp<br/>go ReceiveAnswer"| PN
    MAIN -->|"StartGame"| ORACLE
```

### 2.2 Comunicazione tramite canali

```mermaid
graph LR
    ORACLE(["[goroutine]<br/>Oracle"])
    P0(["[goroutine]<br/>Player 0"])
    P1(["[goroutine]<br/>Player 1"])
    PN(["[goroutine]<br/>Player N"])

    ORACLE -->|"WeakUpChannel buffered(1)"| P0
    ORACLE -->|"WeakUpChannel buffered(1)"| P1
    ORACLE -->|"WeakUpChannel buffered(1)"| PN

    P0 -->|"TryChannel"| ORACLE
    P1 -->|"TryChannel"| ORACLE
    PN -->|"TryChannel"| ORACLE

    ORACLE -->|"AnswerChannel"| P0
    ORACLE -->|"AnswerChannel"| P1
    ORACLE -->|"AnswerChannel"| PN
```

### 2.3 Ciclo di un turno

```mermaid
sequenceDiagram
    participant O as Oracle
    participant P0 as Player 0
    participant P1 as Player 1
    participant PN as Player N

    O->>P0: SendWeakUp(true)
    O->>P1: SendWeakUp(true)
    O->>PN: SendWeakUp(true)

    par Tentativi concorrenti
        P0->>O: SendTry(numero)
        P1->>O: SendTry(numero)
        PN->>O: SendTry(numero)
    end

    alt Tentativo corretto
        O->>P0: SendAnswer(Winner)
        O->>P1: SendLoserPlayers(Loser)
        O->>PN: SendLoserPlayers(Loser)
        Note over O: close(TryChannel) → fine gioco
    else Nessun vincitore
        O->>P0: SendAnswer(hint)
        O->>P1: SendAnswer(hint)
        O->>PN: SendAnswer(hint)
        O->>O: StartGame → nuovo turno
    end
```

---

## 3. Gestione della concorrenza

### 3.1 Non determinismo e un tentativo per turno (R2, R3)

`StartGame` mescola i giocatori con `Shuffle` prima di inviare i `WakeUp`. Poiché i giocatori sono goroutine
indipendenti, l'ordine di arrivo su `TryChannel` rimane non deterministico. Il `WeakUpChannel` è **buffered(1)** per
permettere a `SendWeakUp` di non bloccarsi mentre scorre la lista.

L'Oracolo conta i tentativi ricevuti e avvia il turno successivo solo quando tutti hanno risposto:

```go
countPlayerThatTried++
if countPlayerThatTried == len(startPlayers) {
    countPlayerThatTried = 0
    oracle.StartGame(startPlayers)
}
```

### 3.2 Terminazione pulita (R5)

Alla vittoria l'Oracolo chiude `TryChannel`, ma altre goroutine potrebbero ancora inviare su di esso causando un panic.
La soluzione è un **flag atomico** controllato prima di ogni send:

```mermaid
flowchart LR
    A["SendTry chiamata"] --> B{"closed == 1?"}
    B -->|"sì"| C["return — nessun panic"]
    B -->|"no"| D["TryChannel ← messaggio"]
    E["Vincitore trovato"] --> F["StoreInt32 closed=1"]
    F --> G["close TryChannel"]
    G --> H["ReceiveTries termina"]
```

È fondamentale usare **pointer receiver** (`*OracleImpl`): con value receiver, `StoreInt32` agirebbe su una copia locale
e il flag non sarebbe mai visibile alle altre goroutine.

Alla ricezione di `Winner` / `Loser`, il Player chiude i propri canali terminando le sue goroutine:

```mermaid
flowchart LR
    A["ReceiveAnswer:<br/>Winner o Loser"] --> B["close WeakUpChannel"]
    A --> C["close AnswerChannel"]
    B --> D["ReceiveWeakUp termina"]
    C --> E["ReceiveAnswer termina"]
```

---

## 4. Sviluppo

### 4.1 Interfacce principali

```go
type Oracle interface {
    SecretNumber() int
    StartGame(players []Player)
    SendTry(player Player, number int)
    ReceiveTries(players []Player)
}

type Player interface {
    Name() string
    UI() PlayerUI
    MindNumber(oracle Oracle)
    SendWeakUp(weakUp bool)
    ReceiveWeakUp(oracle Oracle)
    SendAnswer(try TryMessage, answer Answer)
    SendLoserPlayers(try TryMessage, answer Answer)
    ReceiveAnswer()
}
```

### 4.2 Strutture dati

```go
type OracleImpl struct {
    secretNumber   int
    MaxRandomValue int
    TryChannel     chan TryMessage
    closed         int32          // flag atomico
}

type PlayerImpl struct {
    name          string
    ui            PlayerUI
    WeakUpChannel chan WakeUpMessage  // buffered(1)
    AnswerChannel chan AnswerMessage
}
```

### 4.3 Interfaccia grafica

<div style="display: flex; gap: 2%; justify-content: center; ">
    <img src="./menu.png" style="width: 28%;">
    <img src="./players.png" style="width: 70%;">
</div>

La GUI è realizzata con **Fyne**. Ogni Player ha una finestra con label di stato, campo numero, bottone Try e checkbox
bot. Tutte le modifiche UI avvengono tramite `fyne.Do(fun)` (`SafelyUICall`), obbligatorio per rispettare il thread
model di Fyne.

---

## 5. Risultati e considerazioni

Go si è dimostrato adatto per questo problema: goroutine e channel hanno permesso di sincronizzare le entità senza lock
espliciti, mantenendo il codice leggibile. I punti chiave emersi:

- **Pointer receiver**: obbligatorio per operazioni atomiche su strutture condivise tra goroutine.
- **Channel buffered**: evita deadlock nel broadcast di `WakeUp` (un solo slot per giocatore).
- **`range` su channel**: termina il loop automaticamente alla chiusura, semplificando la logica di fine gioco.
- **`fyne.Do`**: unico modo sicuro per aggiornare la UI da goroutine non-main.
