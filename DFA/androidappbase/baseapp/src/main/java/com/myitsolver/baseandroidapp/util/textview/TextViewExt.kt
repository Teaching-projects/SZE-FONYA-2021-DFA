package com.myitsolver.baseandroidapp.util.textview

import android.graphics.*
import android.view.View
import android.widget.TextView
import com.myitsolver.baseandroidapp.util.U

fun TextView.setUnderlined() {
    paintFlags = (paintFlags or Paint.UNDERLINE_TEXT_FLAG)
}
fun TextView.boldStyle() {
    setTypeface(typeface, Typeface.BOLD)
}

fun TextView.normalStyle(){
    setTypeface(Typeface.create(typeface, Typeface.NORMAL), Typeface.NORMAL)
}

var TextView.htmlText: String
get() = text.toString()
set(value) {
    text = U.fromHtml(value, this)
}

fun TextView.isEllipsizedAtTheEnd(): Boolean{
    val l = layout
    if (l != null) {
        val lines = l.lineCount
        if (lines > 0)
            if (l.getEllipsisCount(lines - 1) > 0) return true
    }
    return false
}


fun TextView.gradient(colorStart: Int, colorEnd: Int) {
    val w = if (width == 0) (parent as View).width else width
    val textShader = LinearGradient(w.toFloat()/4, 0f, w.toFloat()*3/4, 0f,
            intArrayOf(colorStart, colorEnd),
            null, Shader.TileMode.CLAMP)
    this.paint.shader = textShader
    invalidate()
}

