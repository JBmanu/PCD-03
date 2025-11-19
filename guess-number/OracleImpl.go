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

// OracleImpl Struct of oracle
type OracleImpl struct {
	secretNumber   int
	MaxRandomValue int
	TryChannel     chan TryMessage
}

// NewOracle Create a new OracleImpl
func NewOracle(maxValue int) OracleImpl {
	return OracleImpl{ComputeRandomNumber(maxValue), maxValue, make(chan TryMessage)}
}

func (oracle OracleImpl) SecretNumber() int {
	return oracle.secretNumber
}

func (oracle OracleImpl) StartGame(players []Player) {
	Foreach(Shuffle(players), func(player Player) {
		player.SendWeakUp(true)
	})
}

func (oracle OracleImpl) SendTry(player Player, number int) {
	defer func() {
		if r := recover(); r != nil {
			fmt.Println("[Closed Channel]", r)
		}
	}()
	oracle.TryChannel <- TryMessage{player, number}
}

func (oracle OracleImpl) ReceiveTries(startPlayers []Player) {
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

		message.Player.SendAnswer(message, answer)

		countPlayerThatTried += 1
		if answer == Winner {
			losers := RemovePlayerFromList(startPlayers, message.Player)
			Foreach(losers, func(loser Player) { loser.SendLoserPlayers(message, Loser) })
			close(oracle.TryChannel)
		} else {
			if countPlayerThatTried == len(startPlayers) {
				countPlayerThatTried = 0
				oracle.StartGame(startPlayers)
			}
		}
	}
	println("Closed OracleImpl")
}
