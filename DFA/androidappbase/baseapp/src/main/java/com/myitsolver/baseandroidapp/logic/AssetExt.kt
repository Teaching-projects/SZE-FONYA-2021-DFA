package com.myitsolver.baseandroidapp.logic

import android.content.Context

fun Context.readJSONFromAsset(name: String): String? {
    var json: String? = null
    try {
        val  inputStream = assets?.open(name)
        json = inputStream?.bufferedReader()?.use{it.readText()}
    } catch (ex: Exception) {
        ex.printStackTrace()
        return null
    }
    return json
}