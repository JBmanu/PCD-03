package main

// Player is an interface for Player
type Player interface {

	// Name of Player
	Name() string

	// UI get ui of player
	UI() PlayerUI

	// MindNumber Mind a random number to guess
	MindNumber(oracle Oracle)

	// SendWeakUp Allow to send at player the current turn
	SendWeakUp(weakUp bool)

	// ReceiveWeakUp Receive the weakUp message
	ReceiveWeakUp(oracle Oracle)

	// SendAnswer Allow to send answer at player
	SendAnswer(try TryMessage, answer Answer)

	// SendLoserPlayers Allow to send the winner at loser
	SendLoserPlayers(try TryMessage, answer Answer)

	// ReceiveAnswer Receive the answer
	ReceiveAnswer()
}

// RemovePlayerFromList Remove a player with same name from list
func RemovePlayerFromList(players []Player, removePlayer Player) []Player {
	var newPlayers []Player
	for _, player := range players {
		if player.Name() != removePlayer.Name() {
			newPlayers = append(newPlayers, player)
		}
	}
	return newPlayers
}