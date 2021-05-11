package com.myitsolver.baseandroidapp.logic

import com.myitsolver.baseandroidapp.views.EditTextValid

class MultipleEditTextValidator{
    private var _currentResult = false

    @Suppress("unused")
    val currentResult
        get() = _currentResult

    var listener: ((Boolean)->Unit)? = null
    set(value) {
        field = value
        listener?.invoke(currentResult)
    }

    private val edittexts = mutableListOf<EditTextValid>()

    fun addEditTextValid(et: EditTextValid) {
        edittexts.add(et)
        et.validityListener = {
            recheckAll()
        }
    }

    fun recheckAll() {
        val newResult = getResult()
        val oldResult = _currentResult
        _currentResult = newResult
        if (oldResult != newResult){
            listener?.invoke(newResult)
        }
    }

    private fun getResult(): Boolean {
        for (e in edittexts){
            if (e.isValid.not()) return false
        }
        return true
    }
}
