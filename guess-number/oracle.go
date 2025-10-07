package main

// TryMessage it's the structure of player message
type TryMessage struct {
	Turn   TurnMessage
	Number int
}

type Oracle struct {
	SecretNumber   int
	MaxRandomValue int
	TryChannel     chan TryMessage
}

// NewOracle create a new Oracle
func NewOracle(maxValue int) Oracle {
	return Oracle{ComputeRandomNumber(maxValue), maxValue, make(chan TryMessage)}
}

// StartGame Oracle call first player that play
func StartGame(players []Player) {
	lenPlayers := len(players) - 1
	shufflePlayers := Shuffle(players)
	playerInPlay := shufflePlayers[lenPlayers]
	playersNotPlay := shufflePlayers[:lenPlayers]
	SendTurnMessage(playerInPlay, playersNotPlay)
}

// SendTryNumberMessage Allow player to send message
func SendTryNumberMessage(oracle Oracle, turn TurnMessage, number int) {
	oracle.TryChannel <- TryMessage{turn, number}
}

// ReceiveTryMessage Allow to receive try player message
func ReceiveTryMessage(oracle Oracle) {
	for message := range oracle.TryChannel {
		var answer Answer
		if message.Number < oracle.SecretNumber {
			answer = TooSmall
		} else if message.Number > oracle.SecretNumber {
			answer = TooBig
		} else {
			answer = Correct
		}
		SendAnswerMessage(message, answer)
		StartGame(message.Turn.PlayersNotPlay)
	}
}
