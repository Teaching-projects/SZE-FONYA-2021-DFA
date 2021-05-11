package com.myitsolver.baseandroidapp.logic

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner


inline fun <T> AppCompatSpinner.addBasicAdapter(data: Array<T>, crossinline toString: ((T)->String),firstHint: Boolean=false,crossinline listener: ((pos: Int, text:String, item: T)->Unit)): ArrayAdapter<String> {
    val dataAdapter = object:ArrayAdapter<String>(context,  android.R.layout.simple_spinner_dropdown_item, data.map(toString)) {
        override fun isEnabled(position: Int): Boolean {
            return !(firstHint && position == 0)
        }

        override fun getDropDownView(
            position: Int, convertView: View?,
            parent: ViewGroup?
        ): View? {
            val view = super.getDropDownView(position, convertView, parent)
            val tv = view as TextView
            if (firstHint && position == 0) { // Set the hint text color gray
                tv.setTextColor(Color.GRAY)
            } else {
                tv.setTextColor(Color.BLACK)
            }
            return view
        }
    }
    adapter = dataAdapter

    setSelection(0)
    onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            listener.invoke(p2,dataAdapter.getItem(p2) ?: "", data[p2])
        }
    }
    return dataAdapter
}

