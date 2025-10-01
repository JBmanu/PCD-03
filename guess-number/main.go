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

	NewMenuUI(myApp, func(count int) {
		// dati
		var players []Player

		// creo i giocatori
		for i := 0; i < count; i++ {
			players = append(players, Player{fmt.Sprintf("p%d", i), secret, make(chan Message)})
		}

		// creo le finestre
		uis := Map(players, func(player Player) *widget.Label { return NewPlayerUI(myApp, player, players) })

		// attivare le goroutine per ogni giocatore
		for i, player := range players {
			go PlayerReceiveMessage(player, uis[i])
		}
	})

	// parte il loop dell'app
	myApp.Run()
}
