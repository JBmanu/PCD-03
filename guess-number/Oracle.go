package main

// Answer Type of response
type Answer int

// Oracle is an interface for OracleImpl
type Oracle interface {
	// SecretNumber get value that compute OracleImpl
	SecretNumber() int

	// StartGame OracleImpl shuffle and weakUp the players
	StartGame(players []Player)

	// SendTry Allow to send message of try at the OracleImpl
	SendTry(player Player, number int)

	// ReceiveTries Receive the try messages
	ReceiveTries(players []Player)
}
