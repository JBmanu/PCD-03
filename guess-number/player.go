package main

import (
	"fmt"
	"strconv"

	"fyne.io/fyne/v2"
)

// Answer Type of response
type Answer int

// Constant value of Answer
const (
	TooSmall Answer = iota
	TooBig
	Winner
	Loser
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

// Player Structure of the player
type Player struct {
	Name          string
	UI            PlayerUI
	WeakUpChannel chan WakeUpMessage
	AnswerChannel chan AnswerMessage
}

// NewPlayerFrom Create players from number
func NewPlayerFrom(myApp fyne.App, oracle Oracle, number int) []Player {
	var players []Player
	for i := 0; i < number; i++ {
		var player Player
		player.Name = fmt.Sprintf("p%d", i)
		player.WeakUpChannel = make(chan WakeUpMessage)
		player.AnswerChannel = make(chan AnswerMessage)
		player.UI = NewPlayerUI(myApp, player, oracle)
		players = append(players, player)
	}
	return players
}

// SendWeakUpMessage Allow to send at player the current turn
func SendWeakUpMessage(player Player, weakUp bool) {
	player.WeakUpChannel <- WakeUpMessage{weakUp}
}

// SendAnswerMessage Allow to send answer at player
func SendAnswerMessage(try TryMessage, answer Answer) {
	info := strconv.Itoa(try.Number) + ". "
	try.Player.AnswerChannel <- AnswerMessage{try, info, answer}
}

// SendLoserPlayers Allow to send the winner
func SendLoserPlayers(try TryMessage, loser Player, answer Answer) {
	info := try.Player.Name + ". "
	loser.AnswerChannel <- AnswerMessage{try, info, answer}
}

// ReceiveWeakUpMessage Receive the current turn
func ReceiveWeakUpMessage(player Player) {
	for message := range player.WeakUpChannel {
		SetInteractionsUI(player.UI, message.WeakUp)
	}
}

// ReceiveAnswerMessage Receive the answer
func ReceiveAnswerMessage(player Player) {
	for message := range player.AnswerChannel {
		infoMessage := message.Info + ToString(message.Answer)
		WhenPlayerReceiveAnswer(player.UI, infoMessage)
		if message.Answer == Loser || message.Answer == Winner {
			close(player.WeakUpChannel)
			close(player.AnswerChannel)
		}
	}
	println("Closed " + player.Name + " channels")
}
