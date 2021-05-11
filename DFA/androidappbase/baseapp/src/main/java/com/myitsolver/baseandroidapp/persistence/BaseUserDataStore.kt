package com.myitsolver.baseandroidapp.persistence

import android.content.Context
import com.myitsolver.baseandroidapp.remote.localGson
import hu.autsoft.krate.SimpleKrate
import hu.autsoft.krate.booleanPref
import hu.autsoft.krate.gson.gson
import hu.autsoft.krate.stringPref


open class BaseUserDataStore(context: Context): SimpleKrate(context,"user_prefs") {
    init {
        gson = localGson
    }

    var firstRun: Boolean by booleanPref("firstRun",true)
    var pushToken: String? by stringPref("pushToken")
    var accessToken: String? by stringPref("accessToken")
}












//import android.content.Context
//import android.content.SharedPreferences
//import com.myitsolver.baseandroidapp.util.fromJson
//import com.myitsolver.baseandroidapp.util.listFromJson
//import com.myitsolver.baseandroidapp.util.toJson
//import me.carleslc.kotlin.extensions.standard.with

//
///**
// *
// * Created by Patrik on 2016. 08. 09..
// */
//open class BaseUserDataStore {
//
//    val isLoggedIn: Boolean
//        get() = accessToken != null
//    val accessToken: String?
//        get() = s!!.getString(ACCESS_TOKEN, null)
//
//    private val userJson: String?
//        get() = s!!.getString(USER_JSON, null)
//
//    var firstRun: Boolean
//        get() = s!!.getBoolean(FIRST_RUN, true)
//        set(value) {
//            s!!.edit().putBoolean(FIRST_RUN, value).apply()
//        }
//    val pushToken: String?
//        get() = s!!.getString(PUSH_TOKEN, null)
//
//
//    fun login(token: String) {
//        saveAccessToken(token)
//    }
//
//    fun saveAccessToken(accessToken: String?) {
//        val e = s!!.edit()
//        if (accessToken != null) {
//            e.putString(ACCESS_TOKEN, accessToken)
//        }else{
//            e.remove(ACCESS_TOKEN)
//        }
//        e.apply()
//    }
//
////    fun <T> getUser(modelClass: Class<T>): T {
////        return BaseRetrofitMain.getGson().fromJson(userJson, modelClass)
////    }
////
////    fun saveUser(user: IUser) {
////        s!!.edit().putString(USER_JSON, BaseRetrofitMain.getGson().toJson(user)).apply()
////    }
//
//    fun logout() {
//        saveAccessToken(null)
//    }
//
//    fun savePushToken(refreshedToken: String) {
//        s!!.edit().putString(PUSH_TOKEN, refreshedToken).apply()
//    }
//
//    @Suppress("UNUSED_PARAMETER")
//    inline fun < reified T> getNonNullLList(withKey: String): MutableList<T> =
//        listFromJson(s?.getString(withKey, "") ?: "")
//
//
//    fun saveObject(o: Any?, withKey: String) {
//
//        s!!.edit().with {
//            if (o != null) {
//                putString(withKey, toJson(o))
//            }else{
//                remove(withKey)
//            }
//        }.apply()
//    }
//
//    inline fun <reified T> getObject(withKey: String, type: Class<T>): T? {
//        val json = s!!.getString(withKey, "") ?: ""
//        if (json.isEmpty() || json.trim().isEmpty()){
//            return null
//        }
//        return fromJson<T>(json)
//    }
//
//    companion object {
//        private val USER_PREFS = "user_prefs"
//        private val USER_JSON = "user_json"
//        private val ACCESS_TOKEN = "access_token"
//        private val FIRST_RUN = "first_run"
//        private val PUSH_TOKEN = "push_token"
//
//        public var s: SharedPreferences? = null
//
//        fun init(context: Context) {
//            s = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
//        }
//    }
//
//
//}
