package com.myitsolver.baseandroidapp.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.myitsolver.baseandroidapp.BaseApplication
import com.myitsolver.baseandroidapp.baseUserDataStore
import com.myitsolver.baseandroidapp.util.NoObserverLiveData
import com.myitsolver.baseandroidapp.util.mutableLiveDataOf
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.ParameterizedType
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

class RetrofitMain<T>(
    baseUrl: String = BaseApplication.getAppBaseConfig().restConfig.apiUrl,
    apiInterface: Class<T>,
    private val apiVersion: String = BaseApplication.getAppBaseConfig().restConfig.apiVersion,
    private val authHeaderName: String = BaseApplication.getAppBaseConfig().restConfig.authHeaderName,
    private val dateFormat: DateFormat

) :
    BaseRetrofitMain<T>(
        baseUrl, apiInterface, Moshi.Builder().apply {
            add(LongLiveDataAdapter())
            add(BooleanLiveDataAdapter())
            add(BooleanNoObserverLiveDataAdapter())
            add(LiveDataAdapter())
            when (dateFormat) {
                is DateFormat.LONG_MS -> add(object {
                    @ToJson()
                    fun toJson(date: Date?): Long? {
                        return date?.time
                    }

                    @FromJson
                    fun fromJson(time: Long?): Date? {
                        time?.let {
                            return Date(it)
                        }
                        return null
                    }
                })
                is DateFormat.LONG_SEC -> add(object {
                    @ToJson()
                    fun toJson(date: Date?): Long? {
                        return date?.time?.div(1000)
                    }

                    @FromJson
                    fun fromJson(time: Long?): Date? {
                        time?.let {
                            return Date(it * 1000)
                        }
                        return null
                    }
                })
                is DateFormat.STRING -> add(Date::class.java, Rfc3339DateJsonAdapter())
            }

            add(KotlinJsonAdapterFactory())
        }.build()
//        GsonBuilder().apply {
//        when (dateFormat) {
//            is DateFormat.LONG_MS -> {
//                registerTypeAdapter(Date::class.java,
//                    JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
//                    .registerTypeAdapter(Date::class.java,
//                        JsonSerializer<Date> { src, typeOfSrc, context -> JsonPrimitive(src.time) })
//            }
//            is DateFormat.LONG_SEC -> {
//                registerTypeAdapter(Date::class.java,
//                    JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong*1000) })
//                    .registerTypeAdapter(Date::class.java,
//                        JsonSerializer<Date> { src, typeOfSrc, context -> JsonPrimitive(src.time/1000) })
//            }
//
//            is DateFormat.STRING -> setDateFormat(dateFormat.format)
//        }
//        addLiveDataSupport()
//    }.create()
    ) {


    private val accesToken: String?
        get() = baseUserDataStore.accessToken


    override val headerExtansion: Request.Builder.() -> Request.Builder = {
        accesToken?.let {
            header(authHeaderName, it)
        }
        header("ApiVersion", apiVersion)
        header("apiversion", apiVersion)
    }


    sealed class DateFormat {
        class LONG_MS : DateFormat()
        class LONG_SEC : DateFormat()
        class STRING(val format: String = "yyyy-MM-dd'T'HH:mm:ss.SSS") : DateFormat()
    }

}

//val defaultGson = GsonBuilder().addLiveDataSupport().create()
val defaultMoshi = Moshi.Builder()
    .add(BooleanLiveDataAdapter()).add(BooleanNoObserverLiveDataAdapter())
    .add(LongLiveDataAdapter()).add(LiveDataAdapter()).add(KotlinJsonAdapterFactory()).build()


open class BaseRetrofitMain<T>(baseUrl: String, apiInterface: Class<T>, val moshi: Moshi) {

    open val headerExtansion: (Request.Builder.() -> Request.Builder) = { this }
    open val requestExtension: (Request.Builder.() -> Request.Builder) = { this }
    //    open val gsonExtension: (GsonBuilder.() -> GsonBuilder) = { this }
    open val clientExtension: (OkHttpClient.Builder.() -> OkHttpClient.Builder) = { this }


    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val originalHttpUrl = original.url
        val url = originalHttpUrl.newBuilder().build()

        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)
            .header("Content-type", "application/json")
            .headerExtansion()
            .method(original.method, original.body)
            .requestExtension()
        chain.proceed(requestBuilder.build())
    }

    private val client
        get() = OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .writeTimeout(12, TimeUnit.HOURS)
            .callTimeout(12, TimeUnit.HOURS)
            .readTimeout(12, TimeUnit.HOURS)

            .clientExtension()
            .build()


    val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val api = retrofit.create(apiInterface)

}

class BooleanLiveDataAdapter {
    @ToJson
    fun toJson(ld: MutableLiveData<Boolean>): Boolean? = ld.value

    @FromJson
    fun fromJson(json: Boolean?): MutableLiveData<Boolean> {
        return mutableLiveDataOf(json)
    }
}

class BooleanNoObserverLiveDataAdapter {
    @ToJson
    fun toJson(ld: NoObserverLiveData<Boolean>): Boolean? = ld.value

    @FromJson
    fun fromJson(json: Boolean?): NoObserverLiveData<Boolean> {
        return NoObserverLiveData<Boolean>().apply {
            postValue(json == true)
        }
    }
}

class LongLiveDataAdapter {
    @ToJson
    fun toJson(ld: MutableLiveData<Long>): Long? = ld.value

    @FromJson
    fun fromJson(json: Long?): MutableLiveData<Long> {
        return mutableLiveDataOf(json)
    }
}

class LiveDataAdapter {
    @ToJson
    inline fun <reified T> toJson(ld: MutableLiveData<T>): String? =
        defaultMoshi.adapter(T::class.java).toJson(ld.value)

    @FromJson
    inline fun <reified T> fromJson(json: Any?): MutableLiveData<T> {
        val ld = MutableLiveData<T>()
        ld.postValue(defaultMoshi.adapter(T::class.java).fromJsonValue(json))
        return ld
    }
}

//
fun GsonBuilder.addLiveDataSupport(): GsonBuilder {
//    registerTypeAdapterFactory(PostProcessingEnabler())
    registerTypeAdapter(MutableLiveData::class.java,
        JsonDeserializer<MutableLiveData<*>> { json, typeOfT, context ->
            val s = typeOfT as ParameterizedType
            val d = MutableLiveData<Any>()
            if (json != null && !json.isJsonNull) {
                d.postValue(context.deserialize<Any>(json, s.actualTypeArguments[0]))
            }
            d
        })
    registerTypeAdapter(NoObserverLiveData::class.java,
        JsonDeserializer<NoObserverLiveData<*>> { json, typeOfT, context ->
            val s = typeOfT as ParameterizedType
            val d = NoObserverLiveData<Any>()
            if (json != null && !json.isJsonNull) {
                d.postValue(context.deserialize<Any>(json, s.actualTypeArguments[0]))
            }
            d
        })
    registerTypeAdapter(MutableLiveData::class.java,
        JsonSerializer<MutableLiveData<*>> { src, typeOfSrc, context ->
            context.serialize(
                src.value,
                (typeOfSrc as ParameterizedType).actualTypeArguments[0]
            )
        })
    registerTypeAdapter(NoObserverLiveData::class.java,
        JsonSerializer<NoObserverLiveData<*>> { src, typeOfSrc, context ->
            context.serialize(
                src.value,
                (typeOfSrc as ParameterizedType).actualTypeArguments[0]
            )
        })
    return this
}

val localGson = GsonBuilder().addLiveDataSupport().create()
