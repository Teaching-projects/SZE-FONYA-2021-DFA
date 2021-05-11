package com.myitsolver.baseandroidapp.util

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

data class AutoCompleteTextModel<T>(val name: String, val data: T){
    override fun toString(): String {
        return name
    }
}

fun <T> AutoCompleteTextView.addBasicAdapter(layout: Int = android.R.layout.select_dialog_item,onItemSelected: ((T)->Unit)?): ArrayAdapter<AutoCompleteTextModel<T>> {
    val acAdapter = ArrayAdapter<AutoCompleteTextModel<T>>(context, layout, mutableListOf())
    setAdapter(acAdapter)
    setOnItemClickListener { _, _, i, _ ->
        onItemSelected?.invoke(acAdapter.getItem(i)?.data ?: return@setOnItemClickListener)
    }
    return acAdapter
}


