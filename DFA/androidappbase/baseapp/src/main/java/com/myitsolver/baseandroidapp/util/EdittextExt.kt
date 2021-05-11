package com.myitsolver.baseandroidapp.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

//class OnTextChangedListener{
//    var afterTextChanged: ((Editable)->Unit)? = null
//    var beforeTextChanged: ((CharSequence?, Int, Int,Int) -> Unit)? = null
//    var textChanged: ((String?) -> Unit)? = null
//}

fun EditText.addTextChangedListener(listener: ((String) -> Unit)): TextWatcher {
    val watcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            listener.invoke(p0?.toString() ?: return)
        }
    }
    this.addTextChangedListener(watcher)
    return watcher
}

var TextView.textString :String?
get() = this.text.toString()
set(value) {
    this.text = value
}