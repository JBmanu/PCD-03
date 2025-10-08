package main

import (
	"fmt"
	"strconv"

	"fyne.io/fyne/v2"
)

type Answer int

const (
	TooSmall Answer = iota
	TooBig
	Correct
	Winner
)

type TurnMessage struct {
	PlayerInPlay   Player
	MissingPlayers []Player
}

type AnswerMessage struct {
	Try    TryMessage
	Info   string
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

// SetEnable Set if player can do an action
func SetEnable(player Player, enable bool) {
	SetInteractionsUI(player.UI, enable)
}

// SendTurnMessage Allow to send at player his turn
func SendTurnMessage(playerInPlay Player, playerNotPlay []Player) {
	playerInPlay.TurnChannel <- TurnMessage{playerInPlay, playerNotPlay}
}

// SendAnswerMessage Send answer at player
func SendAnswerMessage(try TryMessage, answer Answer) {
	info := strconv.Itoa(try.Number) + ". "
	try.Turn.PlayerInPlay.AnswerChannel <- AnswerMessage{try, info, answer}
}

// SendWinnerOtherPlayerNotInPlay Allow to send message
func SendWinnerOtherPlayerNotInPlay(losers []Player, try TryMessage, answer Answer) {
	info := try.Turn.PlayerInPlay.Name + ". "
	Foreach(losers, func(player Player) {
		player.AnswerChannel <- AnswerMessage{try, info, answer}
	})
}

// ReceiveTurnMessage Allow to
func ReceiveTurnMessage(player Player, oracle Oracle) {
	for message := range player.TurnChannel {
		BuildClickButton(oracle, message, player.UI)
	}
}

// ReceiveAnswerMessage Allow player to receive answer message
func ReceiveAnswerMessage(player Player) {
	for message := range player.AnswerChannel {
		infoMessage := message.Info + ToString(message.Answer)
		WhenPlayerReceiveAnswer(player.UI, infoMessage)
	}
}
