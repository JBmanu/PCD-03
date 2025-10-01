package main

import (
	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/widget"
)

func onCheckButton(player Player, players []Player, input *widget.Entry, status *widget.Label) {
	CheckNumberInput(input,
		func(number int) { PlayerSendMessage(player, number, players) },
		func() { status.SetText("Insert a correct number !!") })
}

// NewPlayerUI create UI of the player
func NewPlayerUI(myApp fyne.App, player Player, players []Player) *widget.Label {
	ui := myApp.NewWindow("Player : " + player.Name)
	title := widget.NewLabel("Guess a number between 1 and 100 !!!")
	statusLabel := widget.NewLabel("")
	input := widget.NewEntry()

	button := widget.NewButton("Guess",
		SafelyUIFunc(func() {
			onCheckButton(player, players, input, statusLabel)
		}))

	content := container.NewVBox(title, statusLabel, input, button)
	input.SetPlaceHolder("Enter your number here...")
	ui.SetContent(content)
	ui.Show()

	return statusLabel
}

func checkCountPlayer(input *widget.Entry, startGameClick func(count int)) {
	CheckNumberInput(input,
		func(number int) { startGameClick(number) },
		func() { input.SetPlaceHolder("Insert a correct number !!") })
}

// NewMenuUI create menu to start game
func NewMenuUI(myApp fyne.App, startGameClick func(count int)) {
	ui := myApp.NewWindow("Guess a number play")
	title := widget.NewLabel("Choose a player number")
	input := widget.NewEntry()

	button := widget.NewButton("Go !!",
		SafelyUIFunc(func() {
			ui.Hide()
			checkCountPlayer(input, startGameClick)
		}))

	content := container.NewVBox(title, input, button)
	input.SetPlaceHolder("Number")
	ui.SetContent(content)
	ui.Show()
}
