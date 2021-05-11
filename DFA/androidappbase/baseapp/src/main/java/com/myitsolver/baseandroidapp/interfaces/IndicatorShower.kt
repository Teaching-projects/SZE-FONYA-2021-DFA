package com.myitsolver.baseandroidapp.interfaces

import android.content.DialogInterface

/**
 * Created by Patrik on 2016. 08. 09..
 */
interface IndicatorShower {

    fun isShowingProgressBar(): Boolean

    fun showProgressBar()

    fun hideProgressBar()

    fun showAlertDialog(message: String, listener: DialogInterface.OnClickListener? = null)
}
