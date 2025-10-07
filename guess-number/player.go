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

type TurnMessage struct {
	PlayerInPlay   Player
	PlayersNotPlay []Player
}

type AnswerMessage struct {
	Try    TryMessage
	Answer Answer
}

type EnableMessage struct {
	Enable bool
}

// Player Structure of the player
type Player struct {
	Name          string
	TurnChannel   chan TurnMessage
	EnableChannel chan EnableMessage
	AnswerChannel chan AnswerMessage
}

// NewPlayerFrom Create players from number
func NewPlayerFrom(number int) []Player {
	var players []Player
	for i := 0; i < number; i++ {
		players = append(players, Player{fmt.Sprintf("p%d", i),
			make(chan TurnMessage),
			make(chan EnableMessage),
			make(chan AnswerMessage)})
	}
	return players
}

// SendEnablePlayer Set enable player interactions
func SendEnablePlayer(player Player, enable bool) {
	player.EnableChannel <- EnableMessage{Enable: enable}
}

// SendTurnMessage Allow to send at player his turn
func SendTurnMessage(playerInPlay Player, playerNotPlay []Player) {
	playerInPlay.TurnChannel <- TurnMessage{playerInPlay, playerNotPlay}
}

// SendAnswerMessage Send answer at player
func SendAnswerMessage(try TryMessage, answer Answer) {
	try.Turn.PlayerInPlay.AnswerChannel <- AnswerMessage{try, answer}
}

// ReceiveEnableMessage Receive enable message and enable o disable
func ReceiveEnableMessage(player Player, ui PlayerUI) {
	for message := range player.EnableChannel {
		SetInteractionsUI(ui, message.Enable)
	}
}

// ReceiveTurnMessage Allow to
func ReceiveTurnMessage(player Player, oracle Oracle, ui PlayerUI) {
	for message := range player.TurnChannel {
		Foreach(message.PlayersNotPlay, func(player Player) { SendEnablePlayer(player, false) })
		SendEnablePlayer(message.PlayerInPlay, true)
		BuildClickButton(oracle, message, ui)
	}
}

// ReceiveAnswerMessage Allow player to receive answer message
func ReceiveAnswerMessage(player Player, oracle Oracle, ui PlayerUI) {
	for message := range player.AnswerChannel {
		WhenPlayerReceiveAnswer(ui, message.Answer)
	}
}
