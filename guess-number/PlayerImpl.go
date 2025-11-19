package main

import (
	"fmt"
	"strconv"

	"fyne.io/fyne/v2"
)

// WakeUpMessage Struct of the game progress
type WakeUpMessage struct {
	WeakUp bool
}

// AnswerMessage Struct of the response message
type AnswerMessage struct {
	Try    TryMessage
	Info   string
	Answer Answer
}

// PlayerImpl Structure of the player
type PlayerImpl struct {
	name          string
	ui            PlayerUI
	WeakUpChannel chan WakeUpMessage
	AnswerChannel chan AnswerMessage
}

// NewPlayerFrom Create players from number
func NewPlayerFrom(myApp fyne.App, oracle Oracle, number int) []Player {
	var players []Player
	for i := 0; i < number; i++ {
		var playerImpl PlayerImpl
		playerImpl.name = fmt.Sprintf("p%d", i)
		playerImpl.WeakUpChannel = make(chan WakeUpMessage)
		playerImpl.AnswerChannel = make(chan AnswerMessage)
		playerImpl.ui = NewPlayerUI(myApp, Player(playerImpl), oracle)
		players = append(players, Player(playerImpl))
	}
	return players
}

func (player PlayerImpl) Name() string {
	return player.name
}

func (player PlayerImpl) UI() PlayerUI {
	return player.ui
}

func (player PlayerImpl) MindNumber(oracle Oracle) {
	WaitRandomTimeAndDoAction(3, 5,
		func(waitTime int) {
			SafelyUICall(func() { player.ui.Number.SetText("") })
			fmt.Println("[" + player.Name() + "] Think for " + strconv.Itoa(waitTime) + " s")
		},
		func() {
			randomNumber := ComputeRandomNumber(oracle.SecretNumber()) + 1
			fmt.Println("[" + player.Name() + "] Thought about the " + strconv.Itoa(randomNumber) + " 👋")
			SafelyUICall(func() {
				player.ui.Number.SetText(strconv.Itoa(randomNumber))
				player.ui.TryButton.OnTapped()
			})
		})
}

func (player PlayerImpl) SendWeakUp(weakUp bool) {
	player.WeakUpChannel <- WakeUpMessage{weakUp}
}

func (player PlayerImpl) ReceiveWeakUp(oracle Oracle) {
	for message := range player.WeakUpChannel {
		if player.ui.CheckerBot.Checked {
			player.MindNumber(oracle)
		} else {
			SetInteractionsUI(player.ui, message.WeakUp)
		}
	}
}

func (player PlayerImpl) SendAnswer(try TryMessage, answer Answer) {
	info := strconv.Itoa(try.Number) + ". "
	player.AnswerChannel <- AnswerMessage{try, info, answer}
}

func (player PlayerImpl) SendLoserPlayers(try TryMessage, answer Answer) {
	info := try.Player.Name() + ". "
	player.AnswerChannel <- AnswerMessage{try, info, answer}
}

func (player PlayerImpl) ReceiveAnswer() {
	for message := range player.AnswerChannel {
		infoMessage := message.Info + ToString(message.Answer)
		WhenPlayerReceiveAnswer(player.ui, infoMessage)
		if message.Answer == Loser || message.Answer == Winner {
			close(player.WeakUpChannel)
			close(player.AnswerChannel)
			Close(player.ui)
		}
	}
	println("Closed " + player.name + " channels")
}
