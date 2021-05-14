package com.vidapetra.dfa.automata

data class Dfa (
    var states: List<State>,
    var transitions: List<Transition>
)

data class State (
    var init: Boolean,
    var accept: Boolean
)

data class Transition (
    var from: State,
    var to: State,
    var with: Char
)