@file:Suppress("unused")

package com.myitsolver.baseandroidapp.util

import androidx.lifecycle.*
import com.myitsolver.baseandroidapp.util.recyclerview.BaseRecyclerViewAdapter


class NoObserverLiveData<T>: MutableLiveData<T>(){
    private var localValue: T? = null
    override fun postValue(value: T) {
        super.postValue(value)
        localValue = value
    }

    override fun setValue(value: T) {
        super.setValue(value)
        localValue = value

    }

    override fun getValue(): T? {
        return super.getValue() ?: localValue
    }
}

/**
 * observe and dispatch the value to the observer even if it's null.
 * (The default behavour is to dispatch if it's not null)
 */
fun <T> LiveData<T>.observeAndCall(owner: LifecycleOwner, observer: Observer<T>){
    this.observe(owner, observer)
    if (value == null) {
        observer.onChanged(value)
    }
}
/**
 * observe and dispatch the value to the observer even if it's null.
 * (The default behavour is to dispatch if it's not null)
 */
fun <T> LiveData<T>.observeForeverAndCall(observer: Observer<T>){
    this.observeForever(observer)
    if (value == null) {
        observer.onChanged(value)
    }
}
/**
 * observe and dispatch the value to the observer even if it's null.
 * (The default behavour is to dispatch if it's not null)
 */
fun <T> LiveData<T>.observeAndCall(owner: LifecycleOwner, observer: ((T?)-> Unit)){
    this.observe(owner, androidx.lifecycle.Observer<T> { t -> observer.invoke(t) })
    if (value == null) {
        observer.invoke(value)
    }
}
/**
 * observe and dispatch the value to the observer even if it's null.
 * (The default behavour is to dispatch if it's not null)
 */
fun <T> LiveData<T>.observeForeverAndCall(observer: ((T?)-> Unit)){
    this.observeForever(observer)
    if (value == null) {
        observer.invoke(value)
    }
}

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner,observer: ((T)-> Unit)){
    observe(lifecycleOwner, Observer {
        it?.let{
            observer(it)
        }
    })
}
fun <T> LiveData<T>.observeNullable(lifecycleOwner: LifecycleOwner,observer: ((T?)-> Unit)){
    observe(lifecycleOwner, Observer {
        observer(it)
    })
}


fun <T> MutableLiveData<T>.notifyObservers(){
    this.value = this.value
}

fun <T> MutableLiveData<MutableList<T>>.addItemAndCopy(item: T, pos: Int? = null) {
    val newList = mutableListOf<T>()
    value?.let {
        newList.addAll(it)
    }
    if (pos != null) {
        newList.add(pos,item)
    }else {
        newList.add(item)
    }
    postValue(newList)
}

fun <T> MutableLiveData<MutableList<T>>.removeItemAndCopy(item: T) {
    val newList = mutableListOf<T>()
    value?.let {
        newList.addAll(it)
    }
    newList.remove(item)
    value = newList
}

fun <T> mutableLiveDataOf(data:T?): MutableLiveData<T>{
    val m = MutableLiveData<T>()
    m.postValue(data)
    return m
}
fun <T> mediatorLiveDataOf(data:T?): MediatorLiveData<T>{
    val m = MediatorLiveData<T>()
    m.postValue(data)
    return m
}

inline fun <T> LiveData<T>.observeAsViewHolder(vh: BaseRecyclerViewAdapter.ViewHolder, crossinline observer: ((T?)-> Unit)) {
    observe(vh, observer)
}


