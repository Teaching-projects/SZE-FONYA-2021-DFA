package com.myitsolver.baseandroidapp.util

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)