package com.myitsolver.baseandroidapp


/**
 * Created by Peter on 2018. 01. 16..
 */

abstract class BaseConfig {
    val restConfig: RestConfig = createRestConfig()
    val defaultConfig: DefaultConfig = createConfig()


    abstract fun createRestConfig(): RestConfig

    open fun createConfig(): DefaultConfig = DefaultConfig()


    abstract class RestConfig {
        abstract val apiUrl: String

        open val apiVersion = "1.7"

        open val appId = ""

        open val authHeaderName = "X-AUTH-TOKEN"

        /**
         * return null if the date is sent as long
         * @return
         */
        abstract val dateformat: String?

    }

    open class DefaultConfig {
        open val logTagName = "MyITSolver"
    }

}
