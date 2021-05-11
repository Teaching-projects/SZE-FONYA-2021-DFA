package com.myitsolver.baseandroidapp.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.toDateTimeString(): String{
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    sdf.timeZone = Calendar.getInstance().timeZone
    return sdf.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.toDateString(): String{
    val sdf = SimpleDateFormat("yyyy.MM.dd")
    sdf.timeZone = Calendar.getInstance().timeZone

    return sdf.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.toTimeString(): String{
    val sdf = SimpleDateFormat("HH:mm")
    sdf.timeZone = Calendar.getInstance().timeZone

    return sdf.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.toCustomString(dateFormat: String): String{
    val sdf = SimpleDateFormat(dateFormat)
    sdf.timeZone = Calendar.getInstance().timeZone
    return sdf.format(this)
}