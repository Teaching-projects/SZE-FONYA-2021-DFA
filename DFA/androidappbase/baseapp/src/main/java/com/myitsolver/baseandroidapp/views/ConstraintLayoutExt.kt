package com.myitsolver.baseandroidapp.views

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintLayout.updateConstraints(block: ConstraintSet.() -> Unit) {
    ConstraintSet().apply{
        clone(this@updateConstraints)
        block(this)
        applyTo(this@updateConstraints)
    }
}
