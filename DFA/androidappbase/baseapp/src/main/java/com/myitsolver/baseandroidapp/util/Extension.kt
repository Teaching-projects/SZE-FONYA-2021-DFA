package com.myitsolver.baseandroidapp.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.myitsolver.baseandroidapp.R
import java.text.Normalizer
import kotlin.math.roundToInt


fun Context.dpToPx(dp: Int): Int {
    return U.dpToPx(this, dp)
}

fun Context.spToPx(sp: Int): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        resources.displayMetrics
    ).roundToInt()


fun Fragment.dpToPx(dp: Int): Int {
    if (context != null) {
        return U.dpToPx(context, dp)
    }
    return dp
}

fun parseColorOrNull(color: String?): Int? {
    return try {
        Color.parseColor(color)
    } catch (_: Exception) {
        null
    }
}

fun Context.showCustomDialog(layoutId: Int, cancelable: Boolean = true, showKeyboard: Boolean = false, customAnimation: Int? = null, windowGravity: Int = Gravity.CENTER, fullScreen: Boolean = false, initViews: (Dialog.() -> Unit)? = null): Dialog {
    val dialog = Dialog(this, R.style.dialog)
    try {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layoutId)
        dialog.setCanceledOnTouchOutside(cancelable)
        dialog.setCancelable(cancelable)

        initViews?.invoke(dialog)


        val window = dialog.window
        val wlp = window?.attributes
        wlp?.gravity = windowGravity

        window?.attributes = wlp

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customAnimation?.let {
            window?.attributes?.windowAnimations = it
        }
        if (showKeyboard) {
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
        if (fullScreen) {
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        dialog.show()
    } catch (_: Exception) {

    } finally {
        return dialog
    }
}

fun Double?.prettyPrint(maxDecimals: Int = 2): String {
    if (this == null) return "0"
    val i = toInt()
    return if (this == i.toDouble()) i.toString() else format(maxDecimals)
}

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

fun CharSequence.unaccent(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}


inline fun <R, A> ifNotNull(a: A?, block: (A) -> R): R? =
        if (a != null) {
            block(a)
        } else null

inline fun <R, A, B> ifNotNull(a: A?, b: B?, block: (A, B) -> R): R? =
        if (a != null && b != null) {
            block(a, b)
        } else null

inline fun <R, A, B, C> ifNotNull(a: A?, b: B?, c: C?, block: (A, B, C) -> R): R? =
        if (a != null && b != null && c != null) {
            block(a, b, c)
        } else null

inline fun <R, A, B, C, D> ifNotNull(a: A?, b: B?, c: C?, d: D?, block: (A, B, C, D) -> R): R? =
        if (a != null && b != null && c != null && d != null) {
            block(a, b, c, d)
        } else null