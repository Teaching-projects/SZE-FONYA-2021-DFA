package com.myitsolver.baseandroidapp.util

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.toJpegByteArray(): ByteArray? {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 90, stream)
    val byteArray = stream.toByteArray()
    recycle()
    return byteArray
}
fun Bitmap.toPngByteArray(): ByteArray? {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    recycle()
    return byteArray
}
fun Bitmap.scaleToWidth(newWidth: Int): Bitmap{
    val nh = (height* (newWidth.toDouble() / width)).toInt()
    return Bitmap.createScaledBitmap(this, newWidth, nh, true)
}