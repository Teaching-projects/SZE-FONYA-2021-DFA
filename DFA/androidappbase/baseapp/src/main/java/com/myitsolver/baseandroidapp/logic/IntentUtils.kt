package com.myitsolver.baseandroidapp.logic

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startActivity
import java.lang.Exception
import java.util.*


/**
 * Created by Patrik on 2017. 03. 17..
 */

fun Context.openWebPage(url: String?) {
    url?.let { u ->
        var mUrl = u
        if (!mUrl.startsWith("https://") && !mUrl.startsWith("http://")) {
            mUrl = "http://$mUrl"
        }
        val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mUrl))

        openUrlIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            this.startActivity(openUrlIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Context.shareImageUriAndText(uri: Uri, text: String) {

    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.putExtra(Intent.EXTRA_TEXT, text)
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
    shareIntent.type = "image/jpeg"
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    try {
        startActivity(Intent.createChooser(shareIntent, "Share"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.shareText(text: String) {
    val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text)
    try {
        startActivity(Intent.createChooser(sharingIntent, "Share"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.dial(data: String?) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data, null))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    try {
        this.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.sendMail(subject: String?, message: String?, to: String?) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.data = Uri.parse("mailto:") // only email apps should handle this
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_EMAIL, to)
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, message)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    try {
        this.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openMapsWithCoordinates(
    lat: Double,
    lon: Double,
    zoom: Float = 15.0f,
    name: String = ""
) {
    val uri = String.format(
        Locale.ENGLISH, "geo:%f,%f?z=%f&q=%f,%f (%s)",
        lat, lon, zoom, lat, lon, name
    )
    val intent = Intent(ACTION_VIEW, Uri.parse(uri))
    try {
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


