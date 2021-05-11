package com.myitsolver.baseandroidapp.remote

import kotlinx.coroutines.*

inline fun  <reified T> Deferred<T>.awaitAsBlock() : (suspend  ()->T){
    return {await()}
}

fun onIO( block: suspend (CoroutineScope.()->Unit)): Job {
    return CoroutineScope(Dispatchers.IO).launch {
        block()
    }
}

fun onUI( block: suspend (CoroutineScope.()->Unit)): Job {
    return CoroutineScope(Dispatchers.Main).launch {
        block()
    }
}


fun onUIwithDelay(delay: Long,block: suspend (CoroutineScope.()->Unit)): Job {
    return CoroutineScope(Dispatchers.Main).launch {
        kotlinx.coroutines.delay(delay)
        block()
    }
}