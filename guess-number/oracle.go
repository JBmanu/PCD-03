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

// SendTryNumberMessage Allow player to send message
func SendTryNumberMessage(oracle Oracle, turn TurnMessage, number int) {
	oracle.TryChannel <- TryMessage{turn, number}
}

// ReceiveTryMessage Allow to receive try player message
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

		if answer == Correct {
			SetEnable(message.Turn.PlayerInPlay, false)
			losers := RemovePlayerFromList(startPlayers, message.Turn.PlayerInPlay)
			SendWinnerOtherPlayerNotInPlay(losers, message, Winner)
			SendAnswerMessage(message, answer)
		} else {
			SendAnswerMessage(message, answer)
			if len(message.Turn.MissingPlayers) == 0 {
				StartGame(startPlayers)
			} else {
				callNextPlayer(message.Turn.MissingPlayers)
			}
		}
	}
}
