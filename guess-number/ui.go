package main

import (
	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/widget"
)

type MenuUI struct {
	Window        fyne.Window
	Title         *widget.Label
	MaxRandom     *widget.Entry
	NumberPlayers *widget.Entry
	GoButton      *widget.Button
}

type PlayerUI struct {
	Window    fyne.Window
	Title     *widget.Label
	Info      *widget.Label
	Number    *widget.Entry
	TryButton *widget.Button
}

func BuildClickButton(oracle Oracle, turn TurnMessage, ui PlayerUI) {
	ui.TryButton.OnTapped = func() {
		CheckNumberInput(ui.Number,
			func(number int) { SendTryNumberMessage(oracle, turn, number) },
			func() { ui.Info.SetText("Insert a correct number !!") })
	}
}

// NewPlayerUI create UI of the player
func NewPlayerUI(myApp fyne.App, player Player) PlayerUI {
	var ui PlayerUI
	ui.Window = myApp.NewWindow("Player : " + player.Name)
	ui.Title = widget.NewLabel("Guess a number between 1 and 100 !!!")
	ui.Info = widget.NewLabel("")
	ui.Number = widget.NewEntry()
	ui.TryButton = widget.NewButton("Try", func() {})

	ui.Number.SetPlaceHolder("Enter your number here...")

	content := container.NewVBox(ui.Title, ui.Info, ui.Number, ui.TryButton)
	ui.Window.SetContent(content)
	ui.Window.Show()

	return ui
}

// SetInteractionsUI set interactions of player ui
func SetInteractionsUI(ui PlayerUI, enable bool) {
	SafelyUICall(func() {
		if enable {
			ui.TryButton.Enable()
		} else {
			ui.TryButton.Disable()
		}
	})
}

// WhenPlayerReceiveAnswer Disable button and show answer
func WhenPlayerReceiveAnswer(ui PlayerUI, infoMessage string) {
	SafelyUICall(func() {
		ui.Info.SetText(infoMessage)
		ui.TryButton.Disable()
	})
}

func checkCountPlayer(ui MenuUI, startGameClick func(maxRandom int, numberPlayers int)) {
	CheckNumbersInputs(
		func(values ...int) {
			SafelyUICall(func() { ui.Window.Hide() })
			startGameClick(values[0], values[1])
		},
		func() { ui.MaxRandom.SetPlaceHolder("Insert a correct number !!") },
		ui.MaxRandom, ui.NumberPlayers)
}

// NewMenuUI create menu to start game
func NewMenuUI(myApp fyne.App, startGameClick func(maxValue int, numberPlayers int)) {
	var ui MenuUI
	ui.Window = myApp.NewWindow("Guess a number play")
	ui.Title = widget.NewLabel("Choose parameters:")
	ui.MaxRandom = widget.NewEntry()
	ui.NumberPlayers = widget.NewEntry()
	ui.GoButton = widget.NewButton("Go !!", func() { checkCountPlayer(ui, startGameClick) })

	ui.MaxRandom.SetPlaceHolder("Max value random")
	ui.NumberPlayers.SetPlaceHolder("Number players")

	inputContent := container.NewHSplit(ui.MaxRandom, ui.NumberPlayers)
	content := container.NewVBox(ui.Title, inputContent, ui.GoButton)
	ui.Window.SetContent(content)
	ui.Window.Resize(fyne.Size{Width: 300, Height: 100})
	ui.Window.Show()
}
