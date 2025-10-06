package main

import (
	"fmt"
)

type Answer int

const (
	TooSmall Answer = iota
	TooBig
	Correct
)

type AnswerMessage struct {
	Answer Answer
}

type EnableMessage struct {
	Enable bool
}

// Player Structure of the player
type Player struct {
	Name          string
	EnableChannel chan EnableMessage
	AnswerChannel chan AnswerMessage
}

// NewPlayerFrom Create players from number
func NewPlayerFrom(number int) []Player {
	var players []Player
	for i := 0; i < number; i++ {
		players = append(players, Player{fmt.Sprintf("p%d", i),
			make(chan EnableMessage),
			make(chan AnswerMessage)})
	}
	return players
}

// DisableAllPlayers Disable all players
func DisableAllPlayers(players []Player) {
	Foreach(players, func(player Player) {
		player.EnableChannel <- EnableMessage{Enable: false}
	})
}

// SendAnswerMessage Send answer at player
func SendAnswerMessage(player Player, answer Answer) {
	player.AnswerChannel <- AnswerMessage{answer}
}

// ReceiveEnableMessage Receive enable message and enable o disable
func ReceiveEnableMessage(player Player, ui PlayerUI) {
	for message := range player.EnableChannel {
		SetInteractionsUI(ui, message.Enable)
	}
}

// ReceiveAnswerMessage Allow player to receive answer message
func ReceiveAnswerMessage(player Player, ui PlayerUI) {
	for message := range player.AnswerChannel {
		WhenPlayerReceiveAnswer(ui, message.Answer)
	}
}
