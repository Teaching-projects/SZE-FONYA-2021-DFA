package com.myitsolver.baseandroidapp.util

import android.os.Bundle
import com.google.gson.reflect.TypeToken
import com.myitsolver.baseandroidapp.remote.localGson
import java.io.Serializable
import java.lang.Exception

inline fun Bundle.ifContainsKey(key: String, block: (Bundle.() -> Unit)) {
    if (containsKey(key)) {
        block.invoke(this)
    }
}

inline fun <reified T : Serializable> Bundle.ifContainsEnum(key: String, block: ((T) -> Unit)) {
    if (containsKey(key)) {
        (getSerializable(key) as? T)?.let {
            block.invoke(it)
        }
    }
}

inline fun Bundle.ifContainsInt(key: String, block: ((Int) -> Unit)) {
    if (containsKey(key)) {
        block.invoke(getInt(key))
    }
}

inline fun Bundle.ifContainsLong(key: String, block: ((Long) -> Unit)) {
    if (containsKey(key)) {
        block.invoke(getLong(key))
    }
}

inline fun Bundle.ifContainsBoolean(key: String, block: ((Boolean) -> Unit)) {
    if (containsKey(key)) {
        block.invoke(getBoolean(key))
    }
}

inline fun Bundle.ifContainsString(key: String, block: ((String) -> Unit)) {
    if (containsKey(key)) {
        getString(key)?.let {
            block.invoke(it)
        }
    }
}
inline fun Bundle.ifContainsDouble(key: String, block: ((Double) -> Unit)) {
    if (containsKey(key)) {
        getDouble(key).let {
            block.invoke(it)
        }
    }
}

inline fun <reified T> Bundle.ifContainsObject(key: String, block: ((T) -> Unit)) {
    ifContainsString(key) {
        fromJson<T>(it)?.let {
            block(it)
        }
    }
}

inline fun <reified T> Bundle.ifContainsList(key: String, block: ((MutableList<T>) -> Unit)) {
    ifContainsString(key) {
        block(listFromJson(it))
    }
}


fun toJson(o: Any?): String {
    return localGson.toJson(o)
}

inline fun <reified T> fromJson(json: String): T? = try {
    localGson.fromJson(json, T::class.java)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

inline fun <reified T> listFromJson(json: String): MutableList<T> = try {
    val listType = object : TypeToken<MutableList<T>>() {}.type
    localGson.fromJson(json, listType)
} catch (e: Exception) {
    e.printStackTrace()
    mutableListOf()
}


