//package com.myitsolver.baseandroidapp.remote
//
//import androidx.annotation.Keep
//import com.google.gson.Gson
//import com.google.gson.TypeAdapter
//import com.google.gson.TypeAdapterFactory
//import com.google.gson.reflect.TypeToken
//import com.google.gson.stream.JsonReader
//import com.google.gson.stream.JsonWriter
//import java.io.IOException
//
//@Keep
//interface PostProcessable {
//    fun gsonPostProcess()
//}
//@Keep
//class PostProcessingEnabler : TypeAdapterFactory {
//    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
//        val delegate = gson.getDelegateAdapter(this, type)
//
//        return object : TypeAdapter<T>() {
//            @Throws(IOException::class)
//            override fun write(out: JsonWriter, value: T) {
//                delegate.write(out, value)
//            }
//
//            @Throws(IOException::class)
//            override fun read(`in`: JsonReader): T {
//                val obj = delegate.read(`in`)
//                obj?.let {
//                    if (obj is PostProcessable) {
//                        (obj as PostProcessable).gsonPostProcess()
//                    }
//
//                }
//                return obj
//            }
//        }
//    }
//}
//
//@Target(AnnotationTarget.FIELD)
//@Retention(AnnotationRetention.RUNTIME)
//annotation class LocalLiveData