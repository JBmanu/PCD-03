package main

import (
	"strconv"

	"fyne.io/fyne/v2/widget"
)

type Message struct {
	Name   string
	Number int
}

type Player struct {
	Name    string
	Secret  int
	channel chan Message
}

// allow to send
func playerSendIntoChanel(player *Player, number int, players []*Player) {
	for _, p := range players {
		p.channel <- Message{player.Name, number}
	}
}

// allow to reactive
func playerListenChanel(player *Player, status *widget.Label) {
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

		safetyUIFun(func() { status.SetText(statusStr) })
	}
}
