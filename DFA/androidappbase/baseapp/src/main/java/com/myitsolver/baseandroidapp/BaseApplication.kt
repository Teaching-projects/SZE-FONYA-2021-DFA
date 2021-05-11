package com.myitsolver.baseandroidapp

import androidx.multidex.MultiDexApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.facebook.FacebookSdk

import com.google.firebase.FirebaseApp
import com.myitsolver.baseandroidapp.persistence.BaseUserDataStore
import com.myitsolver.baseandroidapp.util.Logger

/**
 * Created by Patrik on 2017. 07. 19..
 */

abstract class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        firebaseApp = FirebaseApp.initializeApp(this)
        _appBaseConfig = createConfig()
        Logger.init()
        baseUserDataStore = BaseUserDataStore(applicationContext)
    }

    protected abstract fun createConfig(): BaseConfig

    companion object {
        var firebaseApp: FirebaseApp? = null

        private var _appBaseConfig: BaseConfig? = null
        lateinit var baseUserDataStore: BaseUserDataStore

        fun getAppBaseConfig(): BaseConfig {
            if (_appBaseConfig == null) {
                throw RuntimeException("You MUST override BaseApplication and set that as the android application in order to use this function")
            }
            return _appBaseConfig!!
        }
    }

}

internal val baseUserDataStore
    get() = BaseApplication.baseUserDataStore
