package com.myitsolver.baseandroidapp

import android.util.Log
import com.myitsolver.baseandroidapp.util.fromJson
import com.myitsolver.baseandroidapp.util.toJson

fun debug(msg: String?, tag: String = "MyITSolver"){
    ifDebug {
        Log.d(tag, msg)
    }
}

inline fun ifDebug(block: (()->Unit)){
    if (BuildConfig.DEBUG){
        block()
    }
}

inline fun <reified T:Any> T.deepCopy(): T{
    return fromJson(toJson(this))!!
}


