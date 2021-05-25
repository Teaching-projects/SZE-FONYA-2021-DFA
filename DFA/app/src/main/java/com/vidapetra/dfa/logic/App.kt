package com.vidapetra.dfa.logic

import androidx.annotation.Keep
import com.myitsolver.baseandroidapp.BaseApplication
import com.myitsolver.baseandroidapp.BaseConfig


class App : BaseApplication() {


    override fun createConfig() = object : BaseConfig() {
        override fun createRestConfig() = object : RestConfig() {
            override val apiUrl: String
                get() = ""
            override val dateformat: String? = null
        }
    }

    override fun onCreate() {
        super.onCreate()
        Singletons.initAll(this)
    }

}
