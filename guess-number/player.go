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

type SentenceMessage struct {
	Answer Answer
}

type EnableMessage struct {
	Enable bool
}

// Player Structure of the player
type Player struct {
	Name            string
	EnableChannel   chan EnableMessage
	SentenceChannel chan SentenceMessage
}

// NewPlayerFrom create players from number
func NewPlayerFrom(number int) []Player {
	var players []Player
	for i := 0; i < number; i++ {
		players = append(players, Player{fmt.Sprintf("p%d", i),
			make(chan EnableMessage),
			make(chan SentenceMessage)})
	}
	return players
}

// DisableAllPlayers disable all players
func DisableAllPlayers(players []Player) {
	Foreach(players, func(player Player) {
		player.EnableChannel <- EnableMessage{Enable: false}
	})
}

// ReceiveEnableMessage Receive enable message and enable o disable
func ReceiveEnableMessage(player Player, ui PlayerUI) {
	for message := range player.EnableChannel {
		SetInteractionsUI(ui, message.Enable)
	}
}

// ReceiveSentenceMessage Allow player to receive message
func ReceiveSentenceMessage(player Player, ui PlayerUI) {
	for message := range player.SentenceChannel {
		println("CIAO " + ToString(message.Answer))
		SafelyUICall(func() {
			ui.TryButton.Disable()
			ui.Info.SetText(ToString(message.Answer))
		})
	}
}
