package main

import (
	"strconv"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/widget"
)

// MenuUI Struct of a menu
type MenuUI struct {
	Window        fyne.Window
	Title         *widget.Label
	MaxRandom     *widget.Entry
	NumberPlayers *widget.Entry
	GoButton      *widget.Button
}

// PlayerUI Struct of ui player
type PlayerUI struct {
	Window     fyne.Window
	Title      *widget.Label
	Info       *widget.Label
	CheckerBot *widget.Check
	Number     *widget.Entry
	TryButton  *widget.Button
}

func MindNumber(player Player, oracle Oracle) {
	WaitRandomTimeAndDoAction(3, 5, func() {
		randomNumber := ComputeRandomNumber(oracle.SecretNumber) + 1
		SafelyUICall(func() { player.UI.Number.SetText(strconv.Itoa(randomNumber)) })
		SendTryNumberMessage(oracle, player, randomNumber)
	})
}

func clickTryButton(oracle Oracle, player Player, ui PlayerUI) func() {
	return func() {
		CheckNumberInput(ui.Number,
			func(number int) { SendTryNumberMessage(oracle, player, number) },
			func() { ui.Info.SetText("Insert a number !!") })
	}
}

// NewPlayerUI Create UI of the player
func NewPlayerUI(myApp fyne.App, player Player, oracle Oracle) PlayerUI {
	var ui PlayerUI
	ui.Window = myApp.NewWindow("Player : " + player.Name)
	ui.Title = widget.NewLabel("Guess a number between 1 and 100 !!!")
	ui.Info = widget.NewLabel("")
	ui.Number = widget.NewEntry()
	ui.TryButton = widget.NewButton("Try", clickTryButton(oracle, player, ui))
	ui.CheckerBot = widget.NewCheck("Automatic", func(_ bool) {})

	ui.Number.SetPlaceHolder("Enter your number here...")
	ui.CheckerBot.SetChecked(true)

	content := container.NewVBox(ui.Title, ui.Info, ui.CheckerBot, ui.Number, ui.TryButton)
	ui.Window.SetContent(content)
	ui.Window.Show()

	return ui
}

// SetInteractionsUI Set interactions of player ui
func SetInteractionsUI(ui PlayerUI, enable bool) {
	SafelyUICall(func() {
		if enable {
			ui.TryButton.Enable()
		} else {
			ui.TryButton.Disable()
		}
	})
}

// WhenPlayerReceiveAnswer Disable button and show answer when receive answer
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

// NewMenuUI Create menu to start game
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
