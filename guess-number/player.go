package main

import (
	"strconv"

	"fyne.io/fyne/v2/widget"
)

// Message it's the structure of player message
type Message struct {
	Name   string
	Number int
}

// Player Structure of the player
type Player struct {
	Name    string
	Secret  int
	channel chan Message
}

// PlayerSendMessage Allow player to send message
func PlayerSendMessage(player Player, number int, players []Player) {
	Foreach(players, func(p Player) {
		p.channel <- Message{player.Name, number}
	})
}

// PlayerReceiveMessage Allow player to receive message
func PlayerReceiveMessage(player Player, status *widget.Label) {
	for msg := range player.channel {
		number := msg.Number
		var statusStr = msg.Name + " choose " + strconv.Itoa(number) + " "

		if number < player.Secret {
			statusStr += "Too small !"
		} else if number > player.Secret {
			statusStr += "Too High !"
		} else {
			statusStr += "It's correct !!"
		}

		SafelyUICall(func() { status.SetText(statusStr) })
	}
}
