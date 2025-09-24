package main

import (
	"fmt"
	"math/rand"
	"time"

	"fyne.io/fyne/v2/app"
)

type Message struct {
	From string
	Text string
}

func main() {
	rand.New(rand.NewSource(time.Now().UnixNano()))
	secret := rand.Intn(100) + 1

	fmt.Println("Guess number is: ", secret)

	// creo l'app UNA SOLA VOLTA
	myApp := app.New()

	// canale per scambio messaggi
	chat := make(chan Message)

	// creo due finestre giocatore con lo stesso app
	NewUI(myApp, "Giocatore 1", secret)
	NewUI(myApp, "Giocatore 2", secret)

	// goroutine che ascolta i messaggi e li distribuisce
	go func() {
		for msg := range chat {
			fmt.Printf("[%s]: %s\n", msg.From, msg.Text) // log console
			// 🔹 qui puoi anche aggiornare una chat condivisa tra finestre
		}
	}()

	myApp.Run() // parte il loop dell'app
}
