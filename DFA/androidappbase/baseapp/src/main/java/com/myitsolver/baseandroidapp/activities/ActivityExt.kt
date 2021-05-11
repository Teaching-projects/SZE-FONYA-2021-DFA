package com.myitsolver.baseandroidapp.activities

import android.app.Activity
import android.graphics.Color
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.os.Build
import android.view.View

/**
 * dark icons
 *
 */
fun Activity.setStatusbarColorDark() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = window.decorView.systemUiVisibility // get current flag
        flags = flags or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR   // add LIGHT_STATUS_BAR to flag
        window.decorView.systemUiVisibility = flags
//        activity.window.statusBarColor = Color.GRAY // optional
    }
}

/**
 * white icons
 *
 */
fun Activity.setStatusbarColorWhite() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = window.decorView.systemUiVisibility // get current flag
        flags = flags and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        window.decorView.systemUiVisibility = flags
//        activity.window.statusBarColor = Color.GREEN // optional
    }
}