package com.myitsolver.baseapp_branch

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.myitsolver.baseandroidapp.ifDebug
import io.branch.referral.Branch
import io.branch.referral.BranchError
import org.json.JSONObject

open class BranchHandler(private val activity: AppCompatActivity) : LifecycleEventObserver {
    init {
        activity.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                val branch = Branch.getInstance()

                // Branch init
                branch.initSession({ referringParams, error ->
                    if (error == null) {
                        ifDebug { Log.i("BRANCH SDK", referringParams.toString()) }
                        handleBranchResponse(referringParams)
                    } else {
                        ifDebug { Log.i("BRANCH SDK", error.message) }
                        handleBranchError(error)
                    }
                }, activity.intent.data, activity)
            }
            else -> {
            }
        }
    }

    open fun handleBranchResponse(referringParams: JSONObject) {}
    open fun handleBranchError(error: BranchError) {}
}