package main

import (
	"fmt"

	"fyne.io/fyne/v2/app"
)

func main() {
	// creo l'app UNA SOLA VOLTA
	myApp := app.New()

	// Oracle view
	NewMenuUI(myApp, func(maxValue int, numberPlayers int) {
		// create entities
		oracle := NewOracle(maxValue)
		players := NewPlayerFrom(numberPlayers)
		fmt.Println("Guess number is: ", oracle.secretNumber)

		// create players UI
		uis := Map(players, func(player Player) PlayerUI { return NewPlayerUI(myApp, oracle, player) })

		// activate oracle goroutine
		go ReceiveTryMessage(oracle)

		// activate all players goroutine
		for i, player := range players {
			go ReceiveEnableMessage(player, uis[i])
			go ReceiveSentenceMessage(player, uis[i])
		}

		// init game
		DisableAllPlayers(players)
		EnablePlayer(oracle, players)
	})

	// parte il loop dell'app
	myApp.Run()
}
