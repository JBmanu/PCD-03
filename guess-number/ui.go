package main

import (
	"strconv"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/widget"
)

const TITLE = "Guess a number between 1 and 100 !!!"
const INPUT = "Enter your number here..."
const PLAYER = "Player : "
const BUTTON = "Check"
const EMPTY = ""

func onCheckButton(secretNumber int, input *widget.Entry, status *widget.Label) {
	number, err := strconv.Atoi(input.Text)
	if err != nil {
		status.SetText("Insert a correct number !!")
		return
	}

	if number < secretNumber {
		status.SetText("Too small !")
		//chat <- Message{From: name, Text: fmt.Sprintf("Ho provato %d, troppo basso!", guess)}
	} else if number > secretNumber {
		status.SetText("Too High !")
		//chat <- Message{From: name, Text: fmt.Sprintf("Ho provato %d, troppo alto!", guess)}
	} else {
		status.SetText("Correct !!")
		//chat <- Message{From: name, Text: "Ho indovinato il numero! 🎉"}
	}
}

func NewUI(myApp fyne.App, name string, secretNumer int) {
	ui := myApp.NewWindow(PLAYER + name)
	title := widget.NewLabel(TITLE)
	status := widget.NewLabel(EMPTY)
	input := widget.NewEntry()

	safetyClick := func() { fyne.Do(func() { onCheckButton(secretNumer, input, status) }) }

	button := widget.NewButton(BUTTON, safetyClick)
	content := container.NewVBox(title, status, input, button)

	input.SetPlaceHolder(INPUT)

	ui.SetContent(content)
	//ui.Resize(fyne.NewSize(300, 150))
	ui.Show()
}
