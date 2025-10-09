package main

import (
	"math/rand"
	"strconv"
	"time"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/widget"
)

// Map convert elements of array into other type
func Map[T any, R any](in []T, f func(T) R) []R {
	out := make([]R, len(in))
	for i, value := range in {
		out[i] = f(value)
	}
	return out
}

// Foreach do action on each element of array
func Foreach[T any](in []T, action func(T)) {
	for _, value := range in {
		action(value)
	}
}

// SafelyUICall call any func in the Fyne runtime context
func SafelyUICall(fun func()) {
	fyne.Do(fun)
}

// SafelyUIFunc return a func that incorporate a SafelyUICall
func SafelyUIFunc(fun func()) func() {
	return func() {
		SafelyUICall(fun)
	}
}

// ComputeRandomNumber compute random value from maxValue
func ComputeRandomNumber(maxValue int) int {
	random := rand.New(rand.NewSource(time.Now().UnixNano()))
	return random.Intn(maxValue) + 1
}

// Shuffle To shuffle array
func Shuffle[T any](in []T) []T {
	random := rand.New(rand.NewSource(time.Now().UnixNano()))
	shuffledArray := append([]T(nil), in...)
	for i := range shuffledArray {
		j := random.Intn(i + 1)
		shuffledArray[i], shuffledArray[j] = shuffledArray[j], shuffledArray[i]
	}
	return shuffledArray
}

func RemovePlayerFromList(players []Player, removePlayer Player) []Player {
	var newPlayers []Player
	for _, player := range players {
		if player.Name != removePlayer.Name {
			newPlayers = append(newPlayers, player)
		}
	}
	return newPlayers
}

// ToString convert Answer to string
func ToString(answer Answer) string {
	var answerString = [...]string{"It's Too small.", "It's too big.", "It's correct, you win.", "It's winner, you lose."}
	return answerString[answer]
}

// CheckNumberInput check if input is a number and do something
func CheckNumberInput(input *widget.Entry, trueFun func(number int), falseFun func()) {
	number, err := strconv.Atoi(input.Text)
	if err != nil {
		falseFun()
	} else {
		trueFun(number)
	}
}

// CheckNumbersInputs check if all inputs are a number and do something
func CheckNumbersInputs(trueFun func(values ...int), falseFun func(), inputs ...*widget.Entry) {
	var numbers []int
	for _, input := range inputs {
		if number, err := strconv.Atoi(input.Text); err == nil {
			numbers = append(numbers, number)
		}
	}

	if len(numbers) == len(inputs) {
		trueFun(numbers[:]...)
	} else {
		falseFun()
	}
}
