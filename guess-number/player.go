package main

import (
	"fmt"

	"fyne.io/fyne/v2"
)

type Answer int

const (
	TooSmall Answer = iota
	TooBig
	Correct
)

type TurnMessage struct {
	PlayerInPlay   Player
	PlayersNotPlay []Player
}

type AnswerMessage struct {
	Try    TryMessage
	Answer Answer
}

// Player Structure of the player
type Player struct {
	Name          string
	UI            PlayerUI
	TurnChannel   chan TurnMessage
	AnswerChannel chan AnswerMessage
}

// NewPlayerFrom Create players from number
func NewPlayerFrom(myApp fyne.App, number int) []Player {
	var players []Player
	for i := 0; i < number; i++ {
		var player Player
		player.Name = fmt.Sprintf("p%d", i)
		player.TurnChannel = make(chan TurnMessage)
		player.AnswerChannel = make(chan AnswerMessage)
		player.UI = NewPlayerUI(myApp, player)
		players = append(players, player)
	}
	return players
}

// SendTurnMessage Allow to send at player his turn
func SendTurnMessage(playerInPlay Player, playerNotPlay []Player) {
	playerInPlay.TurnChannel <- TurnMessage{playerInPlay, playerNotPlay}
}

// SendAnswerMessage Send answer at player
func SendAnswerMessage(try TryMessage, answer Answer) {
	try.Turn.PlayerInPlay.AnswerChannel <- AnswerMessage{try, answer}
}

// ReceiveTurnMessage Allow to
func ReceiveTurnMessage(player Player, oracle Oracle) {
	for message := range player.TurnChannel {
		Foreach(message.PlayersNotPlay, func(player Player) { SetInteractionsUI(player.UI, false) })
		SetInteractionsUI(player.UI, true)
		BuildClickButton(oracle, message, player.UI)
	}
}

// ReceiveAnswerMessage Allow player to receive answer message
func ReceiveAnswerMessage(player Player) {
	for message := range player.AnswerChannel {
		WhenPlayerReceiveAnswer(player.UI, message.Answer)
	}
}
