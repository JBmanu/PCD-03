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

func safetyUIFun(fun func()) {
	fyne.Do(fun)
}

func onCheckButton(player *Player, players []*Player, input *widget.Entry, status *widget.Label) {
	number, err := strconv.Atoi(input.Text)
	if err != nil {
		status.SetText("Insert a correct number !!")
		return
	}
	playerSendIntoChanel(player, number, players)
}

func NewPlayerUI(myApp fyne.App, player *Player, players []*Player) *widget.Label {
	ui := myApp.NewWindow(PLAYER + player.Name)
	title := widget.NewLabel(TITLE)
	status := widget.NewLabel(EMPTY)
	input := widget.NewEntry()

	safetyClick := func() { fyne.Do(func() { onCheckButton(player, players, input, status) }) }

	button := widget.NewButton(BUTTON, safetyClick)
	content := container.NewVBox(title, status, input, button)

	input.SetPlaceHolder(INPUT)

	ui.SetContent(content)
	ui.Show()

	return status
}

func NewMenuUI(myApp fyne.App) {
	ui := myApp.NewWindow("Guess a number")
	title := widget.NewLabel("Choose a player number")
	input := widget.NewEntry()
	button := widget.NewButton("Go !!", func() {})

	content := container.NewVBox(title, input, button)

	input.SetPlaceHolder("Number")
	ui.SetContent(content)
	ui.Show()
}
