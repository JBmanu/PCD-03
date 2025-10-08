package main

import (
	"fmt"

	"fyne.io/fyne/v2/app"
)

func main() {
	myApp := app.New()

	NewMenuUI(myApp, func(maxValue int, numberPlayers int) {
		// create entities
		oracle := NewOracle(maxValue)
		players := NewPlayerFrom(myApp, numberPlayers)

		fmt.Println("Guess number is: ", oracle.SecretNumber)

		// activate oracle goroutine
		go ReceiveTryMessage(oracle, players)

		// activate all players goroutine
		Foreach(players, func(player Player) {
			go ReceiveTurnMessage(player, oracle)
			go ReceiveAnswerMessage(player)
		})

		// init game
		StartGame(players)
	})

	myApp.Run()
}
