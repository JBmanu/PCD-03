package main

// TryMessage it's the structure of player message
type TryMessage struct {
	Player Player
	Number int
}

type PlayerReceiveAnswerMessage struct {
	Player Player
}

type Oracle struct {
	SortPlayers                []int
	SecretNumber               int
	MaxRandomValue             int
	TryChannel                 chan TryMessage
	PlayerReceiveAnswerChannel chan PlayerReceiveAnswerMessage
}

// NewOracle create a new Oracle
func NewOracle(countPlayer int, maxValue int) Oracle {
	orderedPlayers := make([]int, countPlayer)
	for i := 0; i < countPlayer; i++ {
		orderedPlayers[i] = i
	}
	return Oracle{Shuffle(orderedPlayers),
		ComputeRandomNumber(maxValue),
		maxValue,
		make(chan TryMessage),
		make(chan PlayerReceiveAnswerMessage)}
}

// EnableNextPlayer Oracle choose next random player and it enables
func EnableNextPlayer(oracle Oracle, players []Player) {
	lastValue := oracle.SortPlayers[len(oracle.SortPlayers)-1]
	println(len(oracle.SortPlayers))

	players[lastValue].EnableChannel <- EnableMessage{Enable: true}
	oracle.SortPlayers = oracle.SortPlayers[:len(oracle.SortPlayers)-1]
	println(len(oracle.SortPlayers))
}

// SendTryNumberMessage Allow player to send message
func SendTryNumberMessage(oracle Oracle, player Player, number int) {
	oracle.TryChannel <- TryMessage{player, number}
}

// SendPlayerReceiveAnswerMessage Allow to send receive answer message
func SendPlayerReceiveAnswerMessage(oracle Oracle, player Player) {
	oracle.PlayerReceiveAnswerChannel <- PlayerReceiveAnswerMessage{player}
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

		SendAnswerMessage(message.Player, answer)
	}
}

// ReceivePlayerReceiveAnswerMessage Allow to receive message when player receive answer
func ReceivePlayerReceiveAnswerMessage(oracle Oracle, players []Player) {
	for message := range oracle.PlayerReceiveAnswerChannel {
		SendEnablePlayer(message.Player, false)
		EnableNextPlayer(oracle, players)
	}
}
