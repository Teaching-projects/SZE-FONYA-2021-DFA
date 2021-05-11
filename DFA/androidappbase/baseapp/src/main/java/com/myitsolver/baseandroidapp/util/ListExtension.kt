package com.myitsolver.baseandroidapp.util

fun <T> List<T>.getItemOrNull(index: Int?): T? {
    val i = index ?: return null
    if (i < 0) return null
    if (i < size){
        return get(i)
    }
    return null
}

/**
 * Replace the first occurrence of the give element and returns the index ( -1 if old isnt in the list)
 */
fun <T> MutableList<T>.replace(old: T, new: T): Int{
    val index = indexOf(old)
    if (index != -1 ){
        removeAt(index)
        add(index,new)
    }
    return index
}

fun <T> List<T>?.isEmptyOrNull(): Boolean {
    val l = this ?: return true
    return l.isEmpty()
}

fun <T> MutableList<T>.copy(): MutableList<T> {
    val newList = mutableListOf<T>()
    newList.addAll(this)
    return newList

}

fun <K, V> HashMap<K, V>.getKeyOf(value: V?): K? {
    if (value == null) return null
    val keys = this.keys
    for (k in keys) {
        if (this[k] == value) return k
    }
    return null
}

fun <T> List<T>.indexOfOrNull(item: T?): Int? {
    val it = item ?: return null
    val i = indexOf(it)
    if (i > -1 ){
        return i
    }
    return null
}