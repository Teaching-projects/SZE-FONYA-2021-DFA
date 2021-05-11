package com.myitsolver.baseandroidapp.logic

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.google.android.flexbox.FlexboxLayoutManager

class PagingListener(private val offset: Int = 0,private val scrollBack: Boolean = false,private val insertOnly : Boolean = true, val listener: () -> Unit) : RecyclerView.OnScrollListener() {

    private var loading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val shouldHandle = if (scrollBack) dy < 0 || dx < 0 else dy > 0 || dx > 0
        if (shouldHandle) {
            val lm = recyclerView.layoutManager ?: return


            val visibleItemCount = lm.childCount
            val totalItemCount = lm.itemCount
            val firstVisibleItemPosition = when(lm) {
//                is FlexboxLayoutManager -> lm.findFirstVisibleItemPosition()
                is LinearLayoutManager -> lm.findFirstVisibleItemPosition()
                is GridLayoutManager -> lm.findFirstVisibleItemPosition()
                else -> return
            }

            if (!loading) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - offset
                    && firstVisibleItemPosition >= 0
                ) {
                    loading = true
                    listener()

                    recyclerView.adapter?.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
                        fun datasetChanged(){
                            recyclerView.adapter?.unregisterAdapterDataObserver(this)
                            loading = false
                        }
                        override fun onChanged() {
                            super.onChanged()
                            datasetChanged()
                        }

                        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                            super.onItemRangeInserted(positionStart, itemCount)
                            datasetChanged()
                        }

                        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                            super.onItemRangeChanged(positionStart, itemCount)
                            if (!insertOnly) {
                                datasetChanged()
                            }
                        }

                        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                            if (!insertOnly) {
                                datasetChanged()
                            }
                        }

                        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                            super.onItemRangeRemoved(positionStart, itemCount)
                            if (!insertOnly) {
                                datasetChanged()
                            }
                        }
                    })
                }
            }
        }

    }
}