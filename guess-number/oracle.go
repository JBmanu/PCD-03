package main

// TryMessage it's the structure of player message
type TryMessage struct {
	Player Player
	Number int
}

type Oracle struct {
	CountPlayer    int
	SecretNumber   int
	MaxRandomValue int
	TryChannel     chan TryMessage
}

// NewOracle create a new Oracle
func NewOracle(countPlayer int, maxValue int) Oracle {
	return Oracle{countPlayer, ComputeRandomNumber(maxValue), maxValue, make(chan TryMessage)}
}

// EnableNextPlayer oracle choose next random player and it enables
func EnableNextPlayer(oracle Oracle, players []Player) {
	randomIndex := ComputeRandomNumber(len(players) - 1)
	players[randomIndex].EnableChannel <- EnableMessage{Enable: true}
}

// SendTryNumberMessage Allow player to send message
func SendTryNumberMessage(oracle Oracle, player Player, number int) {
	oracle.TryChannel <- TryMessage{player, number}
}

// ReceiveTryMessage receive try num
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

		SendSentenceMessage(message.Player, answer)
	}
}
