package main

import "fmt"

// Constant value of Answer
const (
	TooSmall Answer = iota
	TooBig
	Winner
	Loser
)

// TryMessage Struct of the try message
type TryMessage struct {
	Player Player
	Number int
}

// Oracle Struct of oracle
type Oracle struct {
	secretNumber   int
	MaxRandomValue int
	TryChannel     chan TryMessage
}

// NewOracle Create a new Oracle
func NewOracle(maxValue int) Oracle {
	return Oracle{ComputeRandomNumber(maxValue), maxValue, make(chan TryMessage)}
}

func (oracle Oracle) SecretNumber() int {
	return oracle.secretNumber
}

func (oracle Oracle) StartGame(players []Player) {
	Foreach(Shuffle(players), func(player Player) {
		SendWeakUpMessage(player, true)
	})
}

func (oracle Oracle) SendTry(player Player, number int) {
	defer func() {
		if r := recover(); r != nil {
			fmt.Println("[Closed Channel]", r)
		}
	}()
	oracle.TryChannel <- TryMessage{player, number}
}

func (oracle Oracle) ReceiveTries(startPlayers []Player) {
	countPlayerThatTried := 0
	for message := range oracle.TryChannel {
		var answer Answer
		switch {
		case message.Number < oracle.SecretNumber():
			answer = TooSmall
		case message.Number > oracle.SecretNumber():
			answer = TooBig
		default:
			answer = Winner
		}

		SendAnswerMessage(message, answer)
		countPlayerThatTried += 1
		if answer == Winner {
			losers := RemovePlayerFromList(startPlayers, message.Player)
			Foreach(losers, func(loser Player) { SendLoserPlayers(message, loser, Loser) })
			close(oracle.TryChannel)
		} else {
			if countPlayerThatTried == len(startPlayers) {
				countPlayerThatTried = 0
				oracle.StartGame(startPlayers)
			}
		}
	}
	println("Closed Oracle")
}
