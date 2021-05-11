package com.myitsolver.baseandroidapp.remote

import android.util.Log
import com.myitsolver.baseandroidapp.models.CommonError
import retrofit2.Response
import java.net.UnknownHostException
import java.util.logging.Logger
import android.os.Looper.getMainLooper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okio.BufferedSink
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import kotlin.math.ceil
import kotlin.math.floor

class LocalError(val message: String?, val code: String?) {



}
typealias ApiError = ((error: LocalError) -> Boolean)

open class BaseRepository<V : CommonError>(private val errorClass: Class<V>) {
    private val runningRequests = mutableListOf<String>()

    var networkError: (() -> Unit)? = null
    private var connection = true
    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        name: String? = null,
        errorBlock: ApiError? = null
    ): T? {
        name?.let {
            if (runningRequests.contains(name)) return null
            runningRequests.add(name)
        }

        val result: Result<T> = safeApiResult(call)
        var data: T? = null

        when (result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                if (errorBlock?.invoke(LocalError(message = result.errorMessage, code = result.errorCode)) != false) { // null and true
                    onError(result.errorCode)
                }
                Log.e("Networking", result.errorCode + " : " + result.errorMessage)
            }
            is Result.NetworkError -> {
                onNetworkError()
                Log.e("Networking", "No internet")
            }
            is Result.LocalError -> {
                result.exception.printStackTrace()
            }
        }

        name?.let {
            runningRequests.remove(name)
        }
        return data
    }


    open fun onError(errorCode: String) {

    }

    open fun onNetworkError() {

    }

    private suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) return Result.Success(response.body()!!)
            val errorBody = response.errorBody()!!.string()
            Log.e("REPO", errorBody)
            if (errorBody.isNotEmpty()) {
                val error: CommonError =
                    defaultMoshi.adapter(errorClass).fromJson(errorBody) as CommonError
                return Result.Error(error.error, error.message)
            }
//            val error: CommonError =
//                defaultMoshi.adapter(errorClass).fromJson(errorBody) as CommonError
//                defaultGson.fromJson(response.errorBody()!!.string(), errorClass)
            return Result.Error("0", "empty body")
//            return Result.Error(error.error, error.message)
        } catch (e: UnknownHostException) {
            return Result.NetworkError(e)
        } catch (e: java.lang.Exception) {
            return Result.LocalError(e)
        }
    }
}

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val errorCode: String, val errorMessage: String) : Result<Nothing>()
    data class NetworkError(val exception: Exception) : Result<Nothing>()
    data class LocalError(val exception: Exception) : Result<Nothing>()
}


class ProgressRequestBody(
    private val mFile: File,
    private val contentTypeString: String,
    private val progressUpdateListener: ((percentage: Int) -> Unit)?
) : RequestBody() {
    private val mPath: String? = null


    override fun contentType(): MediaType? {
        return contentTypeString.toMediaType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var uploaded: Long = 0
        var lastPercentage = -1
        FileInputStream(mFile).use { fis ->
            var read: Int = fis.read(buffer)
            while (read != -1) {

                val percentage = floor(100 * uploaded.toDouble() / (fileLength - 2050)).toInt()
                if (percentage != lastPercentage) {
                    onUI {
                        progressUpdateListener?.invoke(percentage)
                    }
                    lastPercentage = percentage

                }

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = fis.read(buffer)
            }
        }
    }


    companion object {

        private val DEFAULT_BUFFER_SIZE = 2048
    }
}
