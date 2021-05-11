package com.myitsolver.baseandroidapp.util.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.myitsolver.baseandroidapp.util.getItemOrNull

fun RecyclerView.addSnapHelper(): LinearSnapHelper {
    val snapHelper = LinearSnapHelper()
    try {
        snapHelper.attachToRecyclerView(this)
    } catch (_: Exception){

    }
    return snapHelper
}
fun RecyclerView.addPagerSnapHelper(): PagerSnapHelper {
    val snapHelper = PagerSnapHelper()
    try {
        snapHelper.attachToRecyclerView(this)
    } catch (_: Exception){

    }
    return snapHelper
}

fun RecyclerView.addHorizontalLayoutManager(reverse: Boolean = false): LinearLayoutManager {
    val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, reverse)
    layoutManager = lm
    return lm
}

fun RecyclerView.addVerticalLayoutManager(reverse: Boolean = false): LinearLayoutManager {
    val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, reverse)
    layoutManager = lm
    return lm
}

val RecyclerView.linearLayoutManager: LinearLayoutManager?
    get() {
        return layoutManager as? LinearLayoutManager
    }

fun RecyclerView.getFirstVisibleItemPosition(): Int? {
    return linearLayoutManager?.findFirstCompletelyVisibleItemPosition()
}

@Suppress("UNCHECKED_CAST")
fun  <T> RecyclerView.getCurrentItem() : T? {
    return (adapter as? BaseRecyclerViewAdapter<T, *>)?.data?.getItemOrNull(getFirstVisibleItemPosition())
}