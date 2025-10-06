package main

// TryMessage it's the structure of player message
type TryMessage struct {
	Player Player
	Number int
}

type Oracle struct {
	SortPlayers    []int
	SecretNumber   int
	MaxRandomValue int
	TryChannel     chan TryMessage
}

// NewOracle create a new Oracle
func NewOracle(countPlayer int, maxValue int) Oracle {
	orderedPlayer := make([]int, countPlayer)
	for i := 0; i < countPlayer; i++ {
		orderedPlayer[i] = i
	}
	return Oracle{Shuffle(orderedPlayer), ComputeRandomNumber(maxValue), maxValue, make(chan TryMessage)}
}

// EnableNextPlayer oracle choose next random player and it enables
func EnableNextPlayer(oracle Oracle, players []Player) {
	lastValue := oracle.SortPlayers[len(oracle.SortPlayers)-1]
	players[lastValue].EnableChannel <- EnableMessage{Enable: true}
	oracle.SortPlayers = oracle.SortPlayers[:len(oracle.SortPlayers)-1]
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

		SendAnswerMessage(message.Player, answer)
	}
}
