package main

import (
	"fmt"
	"math/rand"
	"time"

	"fyne.io/fyne/v2/app"
	"fyne.io/fyne/v2/widget"
)

func main() {
	rand.New(rand.NewSource(time.Now().UnixNano()))
	secret := rand.Intn(100) + 1

	fmt.Println("Guess number is: ", secret)

	// creo l'app UNA SOLA VOLTA
	myApp := app.New()

	// dati
	count := 3
	var players []*Player
	var uis []*widget.Label

	// creo i giocatori
	for i := 0; i < count; i++ {
		players = append(players, &Player{fmt.Sprintf("p%d", i), secret, make(chan Message)})
	}

	// estraggo i canali
	channels := make([]chan Message, len(players))
	for i, player := range players {
		channels[i] = player.channel
	}

	// creo le finestre
	for _, player := range players {
		uis = append(uis, NewPlayerUI(myApp, player, players))
	}

	// attivare le goroutine per ogni giocatore
	for i, player := range players {
		go playerListenChanel(player, uis[i])
	}

	// parte il loop dell'app
	myApp.Run()
}
