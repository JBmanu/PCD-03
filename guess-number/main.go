package main

import (
	"fmt"

	"fyne.io/fyne/v2/app"
)

func main() {
	myApp := app.New()

	NewMenuUI(myApp, func(maxValue int, numberPlayers int) {

		// create entities
		oracle := Oracle(NewOracle(maxValue))
		players := NewPlayerFrom(myApp, oracle, numberPlayers)

		fmt.Println("[OracleImpl] Guess number: ", oracle.SecretNumber())

		// activate oracle goroutine
		go oracle.ReceiveTries(players)

		// activate all players goroutine
		Foreach(players, func(player Player) {
			go player.ReceiveWeakUp(oracle)
			go player.ReceiveAnswer()
		})

		// init game
		oracle.StartGame(players)
	})

	myApp.Run()
}
