package com.myitsolver.baseandroidapp.views

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import com.myitsolver.baseandroidapp.R
import kotlinx.android.synthetic.main.view_password_valid.view.*

class PasswordValidEditText : BaseView {
    constructor(context: Context?) : super(context) {
        localInit()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        localInit()
        getXmlParams(attrs ?: return )
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        localInit()
        getXmlParams(attrs ?: return )

    }

    fun getXmlParams(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PasswordValidEditText, 0, 0)
        try {
            etPasswordPrivate.backgroundInvalid = ta.getResourceId(R.styleable.PasswordValidEditText_pvetBackgroundInvalid, 0)
            etPasswordPrivate.backgroundValid = ta.getResourceId(R.styleable.PasswordValidEditText_pvetBackgroundValid, 0)
            etPasswordPrivate.drawableInvalid = ta.getResourceId(R.styleable.PasswordValidEditText_pvetDrawableInvalid, 0)
            etPasswordPrivate.drawableValid = ta.getResourceId(R.styleable.PasswordValidEditText_pvetDrawableValid, 0)
            etPasswordPrivate.hint = ta.getString(R.styleable.PasswordValidEditText_android_hint)
            activeId = ta.getResourceId(R.styleable.PasswordValidEditText_pvetDrawableVisible, 0)
            inactiveId = ta.getResourceId(R.styleable.PasswordValidEditText_pvetDrawableInvisible, 0)
        } finally {
            ta.recycle()
        }
    }

    val editText: EditTextValid
        get() = etPasswordPrivate

    @Suppress("MemberVisibilityCanBePrivate")
    var activeId = 0
        set(value) {
            field = value
            changeVisibility()
        }
    @Suppress("MemberVisibilityCanBePrivate")
    var inactiveId = 0
        set(value) {
            field = value
            changeVisibility()
        }

    override fun getLayoutId() = R.layout.view_password_valid

    var isPasswordVisible = false
        set(value) {
            field = value
            changeVisibility()
        }

    private fun changeVisibility() {
        if (isPasswordVisible) {
            if (activeId != 0){
                ivVisible.setImageResource(activeId)
            }
        }else {
            if (inactiveId != 0) {
                ivVisible.setImageResource(inactiveId)
            }
        }
        etPasswordPrivate.inputType = if (isPasswordVisible) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD
        if (!isPasswordVisible) {
            etPasswordPrivate.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            etPasswordPrivate.transformationMethod = null
        }
    }

    private fun localInit() {
        ivVisible.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
        }
    }

}