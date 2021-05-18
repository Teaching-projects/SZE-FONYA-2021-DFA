package com.vidapetra.dfa.automata

data class Dfa (
    var states: List<State>,
    var transitions: List<Transition>,
    var aktState: Int = 0
) {
    fun nextState(char: Char) : Int {
        for(t in transitions){
           if(t.from == aktState && t.with == char){
               return t.to
           }
        }
        return -1
    }

    fun previousState(char: Char) : Int{
        for(t in transitions){
            if(t.to == aktState && t.with == char){
                return t.from
            }
        }
        return -1
    }
}

data class State (
    var init: Boolean,
    var accept: Boolean
)

data class Transition (
    var from: Int,
    var to: Int,
    var with: Char
)