@file:Suppress("ConstantConditionIf", "unused")

package com.myitsolver.baseandroidapp.util

import android.os.Environment
import android.util.Log

import com.myitsolver.baseandroidapp.BaseApplication

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Peter on 2015.08.07..
 */
object Logger {

    private const val log = true
    private const val logToFile = false
    private var TAG = "AppLog"

    fun init() {
        TAG = BaseApplication.getAppBaseConfig().defaultConfig.logTagName
    }


    fun d(o: Any?) {
        if (log) {
            Log.d(TAG, o?.toString())
        }
        if (logToFile) {
            writeToFile("DEBUG - " + o?.toString(), true)
        }
    }

    fun e(o: Any?) {
        if (o is Exception) {
            var s = ""
            if (log) {
                Log.e(TAG, o.toString() + ": " + o.message + " ----- stacktrace below:")
            }
            if (logToFile) {
                s += o.toString() + ": " + o.message + " ----- stacktrace below:"
            }
            for (st in o.stackTrace) {
                if (log) {
                    Log.e(TAG, st.className + " - "
                            + st.methodName + ":" + st.lineNumber)
                }
                if (logToFile) {
                    s += (st.className + " - "
                            + st.methodName + ":" + st.lineNumber + "\n")
                }
            }
            if (logToFile) {
                writeToFile("ERROR - $s", true)
            }
        } else {
            if (log) {
                Log.e(TAG, o?.toString())
            }
            if (logToFile) {
                writeToFile("ERROR - " + o?.toString(), true)
            }
        }
    }

    fun i(o: Any?) {
        if (log) {
            Log.i(TAG, o?.toString())
        }
        if (logToFile) {
            writeToFile("INFO - " + o?.toString(), false)
        }
    }

    fun d(context: Any, o: Any?) {
        if (log) {
            Log.d(TAG, context.javaClass.toString() + " - " + o?.toString())
        }
        if (logToFile) {
            writeToFile("DEBUG - " + context.javaClass.toString() + " - " + o?.toString(), true)
        }
    }

    fun e(context: Any, o: Any?) {
        if (log) {
            Log.e(TAG, context.javaClass.toString() + " - " + o?.toString())
        }
        if (logToFile) {
            writeToFile("ERROR - " + context.javaClass.toString() + " - " + o?.toString(), true)
        }
    }

    fun i(context: Any, o: Any?) {
        if (log) {
            Log.i(TAG, context.javaClass.toString() + " - " + o?.toString())
        }
        if (logToFile) {
            writeToFile("INFO - " + context.javaClass.toString() + " - " + o?.toString(), false)
        }
    }

    fun d(message: String, o: Any?) {
        if (log) {
            Log.d(TAG, message + ": " + o?.toString())
        }
        if (logToFile) {
            writeToFile("DEBUG - " + message + ": " + o?.toString(), true)
        }
    }

    fun e(message: String, o: Any?) {
        if (log) {
            Log.e(TAG, message + ": " + o?.toString())
        }
        if (logToFile) {
            writeToFile("ERROR - " + message + ": " + o?.toString(), true)
        }
    }

    fun i(message: String, o: Any?) {
        if (log) {
            Log.i(TAG, message + ": " + o?.toString())
        }
        if (logToFile) {
            writeToFile("INFO - " + message + ": " + o?.toString(), false)
        }
    }

    private fun writeToFile(s: String, debug: Boolean) {
        val file = getFile(debug)
        try {
            val fw = FileWriter(file, true)
            val bw = BufferedWriter(fw)
            val out = PrintWriter(bw)

            out.println("[" + SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(System.currentTimeMillis())) + "] - " + s)

            out.flush()
            bw.flush()
            fw.flush()

            out.close()
            bw.close()
            fw.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun getFile(debug: Boolean): File {
        val root = File(Environment.getExternalStorageDirectory().toString() + File.separator /*+ BaseConfig.APP_FILES_FOLDER + File.separator */ + "logs" + File.separator)
        root.mkdirs()

        val fname: String
        if (debug) {
            fname = "" + SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(System.currentTimeMillis())) + "_debug.log"
        } else {
            fname = "" + SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(System.currentTimeMillis())) + ".log"
        }

        return File(root, fname)
    }


}
