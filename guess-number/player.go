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
	Correct
	Winner
)

// TurnMessage Struct of the game progress
type TurnMessage struct {
	PlayerInPlay   Player
	MissingPlayers []Player
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

// SendTurnMessage Allow to send at player the current turn
func SendTurnMessage(playerInPlay Player, playerNotPlay []Player) {
	playerInPlay.TurnChannel <- TurnMessage{playerInPlay, playerNotPlay}
}

// SendAnswerMessage Allow to send answer at player
func SendAnswerMessage(try TryMessage, answer Answer) {
	info := strconv.Itoa(try.Number) + ". "
	try.Turn.PlayerInPlay.AnswerChannel <- AnswerMessage{try, info, answer}
}

// SendWinnerOtherPlayerNotInPlay Allow to send the winner
func SendWinnerOtherPlayerNotInPlay(losers []Player, try TryMessage, answer Answer) {
	info := try.Turn.PlayerInPlay.Name + ". "
	Foreach(losers, func(player Player) {
		player.AnswerChannel <- AnswerMessage{try, info, answer}
	})
}

// ReceiveTurnMessage Receive the current turn
func ReceiveTurnMessage(player Player, oracle Oracle) {
	for message := range player.TurnChannel {
		BuildClickButton(oracle, message, player.UI)
	}
}

// ReceiveAnswerMessage Receive the answer
func ReceiveAnswerMessage(player Player) {
	for message := range player.AnswerChannel {
		infoMessage := message.Info + ToString(message.Answer)
		WhenPlayerReceiveAnswer(player.UI, infoMessage)
		if message.Answer == Winner || message.Answer == Correct {
			close(player.TurnChannel)
			close(player.AnswerChannel)
		}
	}
	println("Closed " + player.Name + " channels")
}
