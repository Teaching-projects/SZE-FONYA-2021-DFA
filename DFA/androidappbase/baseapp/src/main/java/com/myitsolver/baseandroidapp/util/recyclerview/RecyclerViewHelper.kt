package com.myitsolver.baseandroidapp.util.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by rishabhkhanna on 14/11/17.
 */
class RecyclerHelper<T>(var list: ArrayList<T>, private var mAdapter: RecyclerView.Adapter<*>) : ItemTouchHelper.Callback() {

    var onDragListener: ((fromPos:Int, toPos:Int)->Unit)? = null
    var onSwipeListener: ((pos: Int)-> Unit)? = null
    private var isItemDragEnabled: Boolean = false
    private var isItemSwipeEnabled: Boolean = false

    private fun onMoved(fromPos: Int, toPos: Int) {
        list.removeAt(toPos)
        mAdapter.notifyItemRemoved(toPos)
    }

    private fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        mAdapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        var dragFlags = 0
        var swipeFlags = 0
        if (isItemDragEnabled) {
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }
        if (isItemSwipeEnabled) {
            swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }
        return makeMovementFlags(dragFlags, swipeFlags)
    }
    

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        onDragListener?.invoke(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onMoved(viewHolder.oldPosition, viewHolder.adapterPosition)
        onSwipeListener?.invoke(viewHolder.position)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    fun setRecyclerItemDragEnabled(isDragEnabled: Boolean): RecyclerHelper<T> {
        this.isItemDragEnabled = isDragEnabled
        return this
    }

    fun setRecyclerItemSwipeEnabled(isSwipeEnabled: Boolean): RecyclerHelper<T> {
        this.isItemSwipeEnabled = isSwipeEnabled
        return this
    }

    fun setOnDragItemListener(onDragListener: ((fromPos:Int, toPos:Int)->Unit)?): RecyclerHelper<T> {
        this.onDragListener = onDragListener
        return this
    }

    fun setOnSwipeItemListener(onSwipeListener: ((pos: Int)-> Unit)?): RecyclerHelper<T> {
        this.onSwipeListener = onSwipeListener
        return this
    }

}