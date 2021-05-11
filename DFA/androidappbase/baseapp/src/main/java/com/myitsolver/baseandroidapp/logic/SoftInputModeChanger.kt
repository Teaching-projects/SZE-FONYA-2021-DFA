package com.myitsolver.baseandroidapp.logic

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class SoftInputModeChanger(private var fragment: Fragment?, private  val softInputMode: Int) :
    LifecycleEventObserver {

    init {
        fragment?.lifecycle?.addObserver(this)
    }

    private var originalSoftInputMode: Int? = null

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
            }
            Lifecycle.Event.ON_START -> {
            }
            Lifecycle.Event.ON_RESUME -> {
                originalSoftInputMode = fragment?.activity?.window?.attributes?.softInputMode
                fragment?.activity?.window?.setSoftInputMode(softInputMode)

            }
            Lifecycle.Event.ON_PAUSE -> {
                originalSoftInputMode?.let {
                    fragment?.activity?.window?.setSoftInputMode(it)
                }

            }
            Lifecycle.Event.ON_STOP -> {
            }
            Lifecycle.Event.ON_DESTROY -> {
                this.fragment?.lifecycle?.removeObserver(this)
                this.fragment = null

            }
            Lifecycle.Event.ON_ANY -> {
            }
        }
    }
}