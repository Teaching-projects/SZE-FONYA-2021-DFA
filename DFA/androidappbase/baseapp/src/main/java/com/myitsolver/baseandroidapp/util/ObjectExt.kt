package com.myitsolver.baseandroidapp.util

fun Any?.ifNull(block: (()->Unit)){
    if (this == null) {
        block.invoke()
    }
}