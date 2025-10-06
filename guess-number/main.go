package main

import (
	"fmt"

	"fyne.io/fyne/v2/app"
)

func main() {
	myApp := app.New()

	// Oracle view
	NewMenuUI(myApp, func(maxValue int, numberPlayers int) {
		// create entities
		oracle := NewOracle(numberPlayers, maxValue)
		players := NewPlayerFrom(numberPlayers)
		uis := Map(players, func(player Player) PlayerUI { return NewPlayerUI(myApp, oracle, player) })

		fmt.Println("Guess number is: ", oracle.SecretNumber)

		// activate oracle goroutine
		go ReceiveTryMessage(oracle)

		// activate all players goroutine
		for i, player := range players {
			go ReceiveEnableMessage(player, uis[i])
			go ReceiveAnswerMessage(player, uis[i])
		}

		// init game
		DisableAllPlayers(players)
		EnableNextPlayer(oracle, players)
	})

	// parte il loop dell'app
	myApp.Run()
}
