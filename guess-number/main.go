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

func main() {
	rand.Seed(time.Now().UnixNano())
	secret := rand.Intn(100) + 1
	attempts := 0

	// creo l'app
	myApp := app.New()
	myWindow := myApp.NewWindow("Guess the Number")

	// label per messaggi
	message := widget.NewLabel("Indovina un numero tra 1 e 100!")
	input := widget.NewEntry()
	input.SetPlaceHolder("Scrivi qui il tuo numero...")

	// bottone per controllare il numero
	button := widget.NewButton("Prova", func() {
		guess, err := strconv.Atoi(input.Text)
		if err != nil {
			message.SetText("Inserisci un numero valido!")
			return
		}

		attempts++

		if guess < secret {
			message.SetText("Troppo basso!")
		} else if guess > secret {
			message.SetText("Troppo alto!")
		} else {
			message.SetText(fmt.Sprintf("Bravo! Hai indovinato in %d tentativi!", attempts))
		}
	})

	// layout verticale
	content := container.NewVBox(
		message,
		input,
		button,
	)

	myWindow.SetContent(content)
	myWindow.Resize(fyne.NewSize(300, 150))
	myWindow.ShowAndRun()
}
