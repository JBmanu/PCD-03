package main

import (
	"strconv"

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

// CheckNumberInput check if input is a number and do something
func CheckNumberInput(input *widget.Entry, trueFun func(number int), falseFun func()) {
	number, err := strconv.Atoi(input.Text)
	if err != nil {
		falseFun()
	} else {
		trueFun(number)
	}
}
