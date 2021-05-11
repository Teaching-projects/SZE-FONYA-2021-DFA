package com.myitsolver.baseandroidapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.myitsolver.baseandroidapp.activities.BaseActivity
import com.myitsolver.baseandroidapp.fragments.BaseFragment

fun <T : ViewModel?> BaseFragment.getViewModelOfFragment(type: Class<T>): T {
    return ViewModelProviders.of(this).get(type)
}
fun <T : ViewModel?> BaseFragment.getViewModelOfActivity(type: Class<T>): T {
    val activity = baseActivity!!
    return ViewModelProviders.of(activity).get(type)
}

fun <T : ViewModel?> BaseActivity.getViewModel(type: Class<T>): T {
    return ViewModelProviders.of(this).get(type)
}
