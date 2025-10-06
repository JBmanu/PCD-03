package main

import (
	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/widget"
)

type PlayerUI struct {
	TitleLabel  *widget.Label
	InfoLabel   *widget.Label
	InputNumber *widget.Entry
	TryButton   *widget.Button
}

func onCheckButton(oracle Oracle, player Player, input *widget.Entry, status *widget.Label) {
	CheckNumberInput(input,
		func(number int) {
			SendTryNumberMessage(oracle, player, number)
		},
		SafelyUIFunc(func() { status.SetText("Insert a correct number !!") }))
}

// NewPlayerUI create UI of the player
func NewPlayerUI(myApp fyne.App, oracle Oracle, player Player) PlayerUI {
	ui := myApp.NewWindow("Player : " + player.Name)
	title := widget.NewLabel("Guess a number between 1 and 100 !!!")
	infoLabel := widget.NewLabel("")
	inputNumber := widget.NewEntry()

	button := widget.NewButton("Try",
		func() { onCheckButton(oracle, player, inputNumber, infoLabel) },
	)

	inputNumber.SetPlaceHolder("Enter your number here...")

	content := container.NewVBox(title, infoLabel, inputNumber, button)
	ui.SetContent(content)
	ui.Show()

	return PlayerUI{title, infoLabel, inputNumber, button}
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

func checkCountPlayer(inputMaxRandom *widget.Entry, inputNumberPlayers *widget.Entry, startGameClick func(maxRandom int, numberPlayers int)) {
	CheckNumbersInputs(
		func(values ...int) { startGameClick(values[0], values[1]) },
		func() { inputNumberPlayers.SetPlaceHolder("Insert a correct number !!") },
		inputMaxRandom, inputNumberPlayers)
}

// NewMenuUI create menu to start game
func NewMenuUI(myApp fyne.App, startGameClick func(maxValue int, numberPlayers int)) {
	ui := myApp.NewWindow("Guess a number play")
	title := widget.NewLabel("Choose parameters:")
	inputMaxRandom := widget.NewEntry()
	inputNumberPlayers := widget.NewEntry()

	button := widget.NewButton("Go !!",
		//SafelyUIFunc(
		func() {
			ui.Hide()
			checkCountPlayer(inputMaxRandom, inputNumberPlayers, startGameClick)
		})
	//)

	inputMaxRandom.SetPlaceHolder("Max value random")
	inputNumberPlayers.SetPlaceHolder("TryNumber player")

	inputContent := container.NewHSplit(inputMaxRandom, inputNumberPlayers)
	content := container.NewVBox(title, inputContent, button)
	ui.SetContent(content)
	ui.Resize(fyne.Size{Width: 300, Height: 100})
	ui.Show()
}
