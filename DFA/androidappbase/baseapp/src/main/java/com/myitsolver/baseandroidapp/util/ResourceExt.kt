package com.myitsolver.baseandroidapp.util

import android.content.Context
import android.graphics.drawable.Drawable

fun Context.getDrawable(name: String): Drawable{
   val resourceId = resources.getIdentifier(name, "drawable",packageName)
    return resources.getDrawable(resourceId)
}

fun Context.getResourceId(name:String): Int{
    return resources.getIdentifier(name, "drawable",packageName)
}