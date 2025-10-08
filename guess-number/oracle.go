package main

// TryMessage Struct of the try message
type TryMessage struct {
	Turn   TurnMessage
	Number int
}

// Oracle Struct of oracle
type Oracle struct {
	SecretNumber   int
	MaxRandomValue int
	TryChannel     chan TryMessage
}

// NewOracle Create a new Oracle
func NewOracle(maxValue int) Oracle {
	return Oracle{ComputeRandomNumber(maxValue), maxValue, make(chan TryMessage)}
}

func callNextPlayer(players []Player) {
	lenPlayers := len(players) - 1
	playerInPlay := players[lenPlayers]
	playersNotPlay := players[:lenPlayers]
	SetEnable(playerInPlay, true)
	Foreach(playersNotPlay, func(player Player) { SetEnable(player, false) })
	SendTurnMessage(playerInPlay, playersNotPlay)
}

// StartGame Oracle call first player that play
func StartGame(players []Player) {
	callNextPlayer(Shuffle(players))
}

// SendTryNumberMessage Allow to send message of try at the Oracle
func SendTryNumberMessage(oracle Oracle, turn TurnMessage, number int) {
	oracle.TryChannel <- TryMessage{turn, number}
}

// ReceiveTryMessage Receive the try messages
func ReceiveTryMessage(oracle Oracle, startPlayers []Player) {
	for message := range oracle.TryChannel {
		var answer Answer
		switch {
		case message.Number < oracle.SecretNumber:
			answer = TooSmall
		case message.Number > oracle.SecretNumber:
			answer = TooBig
		default:
			answer = Correct
		}

		SendAnswerMessage(message, answer)
		if answer == Correct {
			SetEnable(message.Turn.PlayerInPlay, false)
			losers := RemovePlayerFromList(startPlayers, message.Turn.PlayerInPlay)
			SendWinnerOtherPlayerNotInPlay(losers, message, Winner)
		} else {
			if len(message.Turn.MissingPlayers) == 0 {
				StartGame(startPlayers)
			} else {
				callNextPlayer(message.Turn.MissingPlayers)
			}
		}
	}
}
