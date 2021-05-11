package com.myitsolver.chat.messages

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.myitsolver.baseandroidapp.util.recyclerview.addVerticalLayoutManager

class Chat : RecyclerView{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    init {
        addVerticalLayoutManager(true)
    }
}