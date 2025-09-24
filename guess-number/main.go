package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"time"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/app"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/widget"
)

type Message struct {
	From string
	Text string
}

func main() {
	rand.Seed(time.Now().UnixNano())
	secret := rand.Intn(100) + 1

	// creo l'app UNA SOLA VOLTA
	myApp := app.New()

	// canale per scambio messaggi
	chat := make(chan Message)

	// creo due finestre giocatore con lo stesso app
	createPlayerWindow(myApp, "Giocatore 1", secret, chat)
	createPlayerWindow(myApp, "Giocatore 2", secret, chat)

	// goroutine che ascolta i messaggi e li distribuisce
	go func() {
		for msg := range chat {
			fmt.Printf("[%s]: %s\n", msg.From, msg.Text) // log console
			// 🔹 qui puoi anche aggiornare una chat condivisa tra finestre
		}
	}()

	myApp.Run() // parte il loop dell'app
}

func createPlayerWindow(myApp fyne.App, name string, secret int, chat chan<- Message) {
	myWindow := myApp.NewWindow(name)

	status := widget.NewLabel("Indovina un numero tra 1 e 100!")
	input := widget.NewEntry()
	input.SetPlaceHolder("Scrivi qui il tuo numero...")

	button := widget.NewButton("Prova", func() {
		guess, err := strconv.Atoi(input.Text)
		if err != nil {
			status.SetText("Inserisci un numero valido!")
			return
		}

		if guess < secret {
			status.SetText("Troppo basso!")
			chat <- Message{From: name, Text: fmt.Sprintf("Ho provato %d, troppo basso!", guess)}
		} else if guess > secret {
			status.SetText("Troppo alto!")
			chat <- Message{From: name, Text: fmt.Sprintf("Ho provato %d, troppo alto!", guess)}
		} else {
			status.SetText(fmt.Sprintf("Bravo %s! Numero corretto!", name))
			chat <- Message{From: name, Text: "Ho indovinato il numero! 🎉"}
		}
	})

	content := container.NewVBox(
		status,
		input,
		button,
	)

	myWindow.SetContent(content)
	myWindow.Resize(fyne.NewSize(300, 150))
	myWindow.Show()
}
