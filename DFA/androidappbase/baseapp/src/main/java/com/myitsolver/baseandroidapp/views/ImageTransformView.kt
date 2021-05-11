package com.myitsolver.baseandroidapp.views

import android.content.Context
import android.util.AttributeSet
import com.myitsolver.baseandroidapp.R
import kotlinx.android.synthetic.main.view_rotate_image_transform.view.*

class ImageTransformView: BaseView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        getXmlParams(attrs ?: return )
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        getXmlParams(attrs ?: return)
    }

    fun getXmlParams(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ImageTransformView, 0, 0)
        try {
            imgResEnd = ta.getResourceId(R.styleable.ImageTransformView_itvDrawableEnd, 0)
            imgResStart = ta.getResourceId(R.styleable.ImageTransformView_itvDrawableStart, 0)
            startOffsetInDegree = ta.getInteger(R.styleable.ImageTransformView_itvOffsetStart, 0)
            endOffsetInDegree = ta.getInteger(R.styleable.ImageTransformView_itvOffsetEnd, 0)

        } finally {
            ta.recycle()
        }
    }
    override fun getLayoutId() = R.layout.view_rotate_image_transform

    var state = State.START

    var endOffsetInDegree: Int = 0
    var startOffsetInDegree: Int = 0

    var imgResStart: Int = 0
        set(value) {
            field = value
            ivDefault.setImageResource(value)
        }
    var imgResEnd: Int = 0
        set(value) {
            field = value
            ivTransformed.setImageResource(value)
        }
    var fixed = false
    var percent: Float = 0f
        set(value) {
            if (!fixed) {
                field = if (value < 0f) {
                    0f
                } else if (value > 1f) {
                    1f
                } else {
                    value
                }
                calcState()
                doTransformation()

            }
        }

    private fun calcState() {
        state = if (percent < 0.01f){
            State.START
        }else if (percent > 0.99f){
            State.END
        }else {
            State.ANIMATING
        }
    }

    enum class State{
        START, ANIMATING, END
    }

    private fun doTransformation() {
        ivDefault.visibleOrGone(state == State.START || state == State.ANIMATING)
        ivTransformed.visibleOrGone(state == State.END || state == State.ANIMATING)

        ivDefault.rotation = startOffsetInDegree + 180*percent
        ivTransformed.rotation = endOffsetInDegree + 180+180*percent
        ivDefault.alpha = 1-percent
        ivTransformed.alpha = percent


    }
}