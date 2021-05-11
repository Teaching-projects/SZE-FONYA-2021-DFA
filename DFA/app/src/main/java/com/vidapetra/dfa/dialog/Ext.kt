package com.vidapetra.dfa.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import com.myitsolver.baseandroidapp.remote.onUI
import com.myitsolver.baseandroidapp.util.showCustomDialog
import com.myitsolver.baseandroidapp.util.textString
import com.myitsolver.baseandroidapp.views.inflate
import com.myitsolver.baseandroidapp.views.visibleOrGone
import com.vidapetra.dfa.logic.Toastr
import com.vidapetra.dfa.logic.applicationContext
import com.vidapetra.dfa.R
import kotlinx.android.synthetic.main.dialog_frame.*
import kotlinx.android.synthetic.main.dialog_info.*
import kotlinx.android.synthetic.main.dialog_laoding.*
import kotlinx.android.synthetic.main.dialog_text_input.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Context.showBottomDialog(layoutId: Int, title: String, showKeyboard: Boolean = false,backgroundColor: Int = Color.WHITE, initViews: (Dialog.()->Unit)? = null, listener: ((Boolean, Dialog)->Unit)? = null) {
    showCustomDialog(R.layout.dialog_bottom_frame,showKeyboard = showKeyboard, windowGravity = Gravity.BOTTOM) {
        container.inflate(layoutId, true)
        tvTitle.text = title
        setCanceledOnTouchOutside(true)
        initViews?.invoke(this)
        container.invalidate()
        container.requestLayout()
        findViewById<CardView>(R.id.card).setCardBackgroundColor(backgroundColor)

        findViewById<AppCompatImageView>(R.id.btnCancel).setOnClickListener {
            dismiss()
            listener?.invoke(false, this)
        }


    }
}
fun Context.showDialog(layoutId: Int, title: String, yesText : String? = null, noText: String? = null, showKeyboard: Boolean = false, initViews: (Dialog.()->Unit)? = null, listener: ((Boolean, Dialog)->Unit)? = null): Dialog {
    return showCustomDialog(R.layout.dialog_frame,showKeyboard = showKeyboard) {
        container.inflate(layoutId, true)
        tvTitle.text = title
        tvTitle.visibleOrGone(title.isNotBlank())
        setCanceledOnTouchOutside(true)
        initViews?.invoke(this)
        container.invalidate()
        container.requestLayout()
        btnOk.visibleOrGone(yesText != null)
        btnCancel.visibleOrGone(noText != null)
        yesText?.let {
            btnOk.text = it
        }
        btnOk.setOnClickListener {
            dismiss()
            listener?.invoke(true, this)
        }
        noText?.let{
            btnCancel.text = it
        }
        btnCancel.setOnClickListener {
            dismiss()
            listener?.invoke(false, this)
        }
    }
}

fun Context.showLoadingDialog(message: String?): Dialog{
    return showCustomDialog(R.layout.dialog_frame,cancelable = false) {
        container.inflate(R.layout.dialog_laoding, true)
        tvTitle.text = getString(R.string.loading)
        tvMessage.text = message
        tvMessage.visibleOrGone(message != null)
        container.invalidate()
        container.requestLayout()
        btnOk.visibleOrGone(false)
        btnCancel.visibleOrGone(false)
    }
}

fun Context.showInfoDialog(title:String?, message: String?, yesText: String = getString(R.string.ok), listener: ((Boolean,Dialog)->Unit)? = null): Dialog {
    return showDialog(R.layout.dialog_info, yesText = getString(R.string.ok), title = title ?: "",listener = listener, initViews = {
        tvInfo.text = message
    })

}



fun Context.showTextInputDialog(title:String?,  yesText: String = getString(R.string.ok), noText: String? = getString(R.string.cancel), listener: ((String)->Unit)? = null): Dialog {
    return showDialog(R.layout.dialog_text_input, yesText = getString(R.string.ok), noText = noText, title = title ?: "") {yes, dialog ->
        if (yes) {
            listener?.invoke(dialog.editText.textString ?: "")
        }
    }
}

fun Context.showQuestionDialog(title:String?, message: String?, yesText: String = getString(R.string.ok),noText: String = getString(R.string.cancel), listener: ((Boolean)->Unit)? = null): Dialog {
    return showCustomDialog(R.layout.dialog_frame) {
        container.inflate(R.layout.dialog_info, attachToRoot = true)
        tvTitle.visibleOrGone(title != null)
        tvTitle.text = title
        tvInfo.visibleOrGone(message != null)
        tvInfo.text = message
        btnCancel.text = noText
        btnCancel.setOnClickListener {
            dismiss()
            listener?.invoke(false)
        }
        btnOk.text = yesText
        btnOk.setOnClickListener {
            dismiss()
            listener?.invoke(true)
        }
    }
}




fun onIoWithLoading(title: String = applicationContext.getString(R.string.loading), block: suspend (CoroutineScope.() -> Unit)): Job {
    return CoroutineScope(Dispatchers.IO).launch {
        onUI {
            Toastr.showLoadingDialog(title)
        }
        block()
        onUI {
            Toastr.hideLoadingDialog()
        }
    }
}

