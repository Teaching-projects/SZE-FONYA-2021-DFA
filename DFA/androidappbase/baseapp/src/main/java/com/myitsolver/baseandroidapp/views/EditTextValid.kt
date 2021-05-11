package com.myitsolver.baseandroidapp.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent

import java.util.regex.Pattern
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputEditText
import com.myitsolver.baseandroidapp.R
import com.myitsolver.baseandroidapp.util.addTextChangedListener

@Suppress("MemberVisibilityCanBePrivate")
/**
 * Created by Kodam on 2018. 01. 27..
 */

class EditTextValid : TextInputEditText {

    private var valid = false

    var validityListener: ((valid: Boolean)->Unit)
        get() {
            throw Exception("Setter only")
        }
    set(value) {
            validityListeners.add(value)
    }

    val validityListeners = mutableListOf<((valid: Boolean)->Unit)>()

    var isValid: Boolean
        get() = valid
        set(valid) {
            val oldValid = this.valid
            this.valid = valid

            if (oldValid != this.valid){
                for (l in validityListeners){
                    l.invoke(valid)
                }
            }

            if (valid) {
                setValid()
            } else {
                setInvalid()
            }
        }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getXmlParams(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getXmlParams(attrs)
    }

    var backgroundInvalid: Int = 0
    var backgroundValid: Int = 0
    var drawableInvalid: Int = 0
    var drawableValid: Int = 0

    fun getXmlParams(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.EditTextValid, 0, 0)
        try {
            backgroundInvalid = ta.getResourceId(R.styleable.EditTextValid_etvBackgroundInvalid, 0)
            backgroundValid = ta.getResourceId(R.styleable.EditTextValid_etvBackgroundValid, 0)
            drawableInvalid = ta.getResourceId(R.styleable.EditTextValid_etvDrawableInvalid, 0)
            drawableValid = ta.getResourceId(R.styleable.EditTextValid_etvDrawableValid, 0)
        } finally {
            ta.recycle()
        }
    }

    fun setValidatorByPattern(pattern: Pattern) {
        this.addTextChangedListener {
            val valid = pattern.matcher(it).matches()
            isValid = valid
        }

    }

    fun setValidatorByRegex(regex: String) {
        val pattern = Pattern.compile(regex)
        setValidatorByPattern(pattern)
    }

    fun setValidatorByEditTextValid(editTextValid: EditTextValid) {
        editTextValid.addTextChangedListener {
            val valid = isValid && editableText.toString() == it
            isValid = valid
        }

        this.addTextChangedListener {
            val other = editTextValid.editableText.toString()
            val otherValid = editTextValid.isValid
            val valid =  otherValid && other == it
            isValid = valid
        }
    }

    private fun setValid() {
        if ( backgroundValid != 0) {
            background = context.resources.getDrawable(backgroundValid)
        }
        if ( drawableValid != 0) {
            setCompoundDrawables(null, null, getDrawable(drawableValid), null)
        }
//        setCompoundDrawables(null, null, getDrawable(R.drawable.checksign), null)

    }

    private fun setInvalid() {
        if ( backgroundInvalid != 0) {
            background = context.resources.getDrawable(backgroundInvalid)
        }
        if ( drawableInvalid != 0) {
            setCompoundDrawables(null, null, getDrawable(drawableInvalid), null)
        }

////        background = context.resources.getDrawable(R.drawable.bg_et_white)
//        setCompoundDrawables(null, null, getDrawable(R.drawable.wrong_pw_icon_x), null)
    }

    private fun getDrawable(id: Int): Drawable {
        val image = context.resources.getDrawable(id)
        val h = (image.intrinsicHeight * 0.8).toInt()
        val w = (image.intrinsicWidth * 0.8).toInt()
        image.setBounds(0, 0, w, h)
        return image
    }

    fun clearFlag() {
        valid = true
        setCompoundDrawables(null, null, null, null)
    }


}


fun AppCompatEditText.addRightDrawableListener(listener: (()->Unit)){
    setOnTouchListener { view, motionEvent ->
       when (motionEvent.action){
           MotionEvent.ACTION_UP -> {
               if (motionEvent.getX() >= (right - compoundDrawables[2].bounds.width())) {
                   listener.invoke()
               }
               return@setOnTouchListener true
           }
           else -> {return@setOnTouchListener false}

       }
    }
}