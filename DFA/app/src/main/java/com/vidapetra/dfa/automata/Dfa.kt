package com.vidapetra.dfa.automata

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vidapetra.dfa.logic.applicationContext
import java.io.IOException

data class Dfa (
    var id: Int,
    var states: List<State>,
    var transitions: List<Transition>,
    var abc: List<Char>,
    var description: String,
    var imageName: String = "",
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
    var id: Int,
    var init: Boolean,
    var accept: Boolean
)

data class Transition (
    var from: Int,
    var to: Int,
    var with: Char
)

fun getDfaListFromAssets(context: Context) : List<Dfa>{
    val jsonString: String
    try {
        jsonString = context.assets.open("dfa_list.json").bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return listOf()
    }
    val gson = Gson()
    val dfaListType = object : TypeToken<List<Dfa>>() {}.type

    return gson.fromJson(jsonString, dfaListType)
}