package main

// Answer Type of response
type Answer int

// OracleInterface is a interface for Oracle
type OracleInterface interface {
	// SecretNumber get value that compute Oracle
	SecretNumber() int

	// StartGame Oracle shuffle and weakUp the players
	StartGame(players []Player)

	// SendTry Allow to send message of try at the Oracle
	SendTry(player Player, number int)

	// ReceiveTries Receive the try messages
	ReceiveTries(players []Player)
}
