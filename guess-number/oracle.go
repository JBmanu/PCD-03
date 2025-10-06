package main

import (
	"math/rand"
	"time"
)

// TryNumberMessage it's the structure of player message
type TryNumberMessage struct {
	Player    Player
	TryNumber int
}

type Oracle struct {
	maxValue     int
	secretNumber int
	TryChannel   chan TryNumberMessage
}

func computeRandomNumber(maxValue int) int {
	seed := rand.New(rand.NewSource(time.Now().UnixNano()))
	return seed.Intn(maxValue) + 1
}

// NewOracle create a new Oracle
func NewOracle(maxValue int) Oracle {
	return Oracle{maxValue, computeRandomNumber(maxValue), make(chan TryNumberMessage)}
}

// EnablePlayer oracle choose random player and it enables
func EnablePlayer(oracle Oracle, players []Player) {
	randomIndex := computeRandomNumber(len(players) - 1)
	players[randomIndex].EnableChannel <- EnableMessage{Enable: true}
}

// SendTryNumberMessage Allow player to send message
func SendTryNumberMessage(oracle Oracle, player Player, tryNumber int) {
	oracle.TryChannel <- TryNumberMessage{player, tryNumber}
}

// ReceiveTryMessage receive try num
func ReceiveTryMessage(oracle Oracle) {
	for message := range oracle.TryChannel {
		var answer Answer
		if message.TryNumber < oracle.secretNumber {
			answer = TooSmall
		} else if message.TryNumber > oracle.secretNumber {
			answer = TooBig
		} else {
			answer = Correct
		}

		message.Player.SentenceChannel <- SentenceMessage{answer}
	}
}
