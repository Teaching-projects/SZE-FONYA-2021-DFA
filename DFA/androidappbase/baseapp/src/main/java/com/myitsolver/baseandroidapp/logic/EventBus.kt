package com.myitsolver.baseandroidapp.logic

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.myitsolver.baseandroidapp.util.observeAndCall
import com.myitsolver.baseandroidapp.util.observeForeverAndCall

open class EventBus{

    private val buses = HashMap<String,EventBusLiveData>()

    fun getBus(name: String): EventBusLiveData {
        if (buses.containsKey(name).not()){
            buses[name] = EventBusLiveData()
        }
        return buses[name]!!
    }

    fun removeEventBus(name:String){
        buses.remove(name)
    }
}

class EventBusLiveData: MutableLiveData<Any>(){
    inline fun <reified T> observeEvent(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observeAndCall(lifecycleOwner, Observer {
            (it as? T)?.let{
                observer.onChanged(it)
                clear()
            }
        })
    }
    inline fun <reified T> observeEventForever( observer: Observer<T>) {
        observeForeverAndCall(Observer {
            (it as? T)?.let{
                observer.onChanged(it)
                clear()
            }
        })
    }

    fun clear() {
        this.postValue(null)
    }
}