package com.myitsolver.baseandroidapp.util.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.fiftyshadesof.FiftyShadesOf
import com.myitsolver.baseandroidapp.R
import com.myitsolver.baseandroidapp.remote.onUI
import com.myitsolver.baseandroidapp.views.inflate
import com.myitsolver.baseandroidapp.views.startLoadingAnimation
import kotlinx.android.extensions.LayoutContainer


abstract class BaseDiffRecyclerviewAdapter<T>(hasAnimation: Boolean = true) :
    DiffAnimatedRecyclerviewAdapter<T, BaseRecyclerViewAdapter.ViewHolder>(hasAnimation) {

    open val placeHolderId: Int = R.layout.item_placeholder

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == -1) return ViewHolder(
            parent.inflate(placeHolderId)
        )
        return ViewHolder(
            parent.inflate(
                getViewHolderLayoutId(viewType)
            )
        )
    }

    final override fun getItemViewType(position: Int): Int {
        return if (shouldShowPlaceholder()) -1
        else getViewType(position)
    }

    override fun getItemCount(): Int {
        return if (shouldShowPlaceholder()) 1 else data?.size ?: 1
    }

    open fun shouldShowPlaceholder(): Boolean {
        return data?.size ?: 0 == 0
    }

    open fun onBindPlaceHolder(holder: ViewHolder) {

    }

    abstract fun getViewHolderLayoutId(viewType: Int): Int

    open fun getViewType(position: Int): Int {
        return 0
    }

}

abstract class BaseDatabindingDiffRecyclerviewAdapter<T>(hasAnimation: Boolean = true) :
    DiffAnimatedRecyclerviewAdapter<T, BaseRecyclerViewAdapter.DataBindingViewHolder>(hasAnimation) {

    open val placeHolderId: Int = R.layout.item_placeholder

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        Log.d("ooo", "create vh for :" + hashCode())
        if (viewType == -1) return DataBindingViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context!!), placeHolderId, parent, false)
        )
        return DataBindingViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context!!),
                getViewHolderLayoutId(viewType),
                parent,
                false
            )
        )
    }

    final override fun getItemViewType(position: Int): Int {
        return if (shouldShowPlaceholder()) -1
        else getViewType(position)
    }

    override fun getItemCount(): Int {
        return if (shouldShowPlaceholder()) 1 else data?.size ?: 1
    }

    open fun shouldShowPlaceholder(): Boolean {
        return (data?.size ?: 0) == 0
    }


    abstract fun getViewHolderLayoutId(viewType: Int): Int

    open fun getViewType(position: Int): Int {
        return 0
    }

}


abstract class DiffAnimatedRecyclerviewAdapter<T, VH : BaseRecyclerViewAdapter.ViewHolder>(val hasAnimation: Boolean = true) :
    DiffRecyclerViewAdapter<T, VH>() {
    private var loadAnimation: FiftyShadesOf? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (data == null) {
            if (hasAnimation) {
                loadAnimation = holder.itemView.startLoadingAnimation()
            }
        } else {
            loadAnimation?.let {
                loadAnimation?.stop()
                loadAnimation = null
            }
            addListener(holder, position)
            onBindViewHolder(holder, position, getItemViewType(position))
        }
    }

    abstract fun onBindViewHolder(holder: VH, position: Int, viewType: Int)
}

abstract class DiffRecyclerViewAdapter<T, VH : BaseRecyclerViewAdapter.ViewHolder> :
    BaseRecyclerViewAdapter<T, VH>() {
    var oldData: List<T>? = null

    override var data: List<T>? = null
        set(value) {
            oldData = field
            field = value
            DiffUtil.calculateDiff(diffUtil, true).dispatchUpdatesTo(this@DiffRecyclerViewAdapter)
        }

    open val diffUtil = object : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return if (oldData == null && data?.isNotEmpty() != true) false else areItemsTheSame(
                oldData?.getOrNull(oldItemPosition),
                data?.getOrNull(newItemPosition)
            )
        }

        override fun getOldListSize(): Int {
//            return oldData?.size ?: 1
            return if (oldData?.size == 0) 1 else oldData?.size ?: 1
        }

        override fun getNewListSize(): Int {
//            return data?.size ?: 1
            return if (data?.size == 0) 1 else data?.size ?: 1
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return if (oldData == null && data?.isNotEmpty() != true) false else areContentsTheSame(
                oldData?.getOrNull(oldItemPosition),
                data?.getOrNull(newItemPosition)
            )
        }

    }

    open fun areItemsTheSame(oldItem: T?, newItem: T?): Boolean {
        return if (oldItem == null && newItem == null) false else oldItem == newItem
    }

    open fun areContentsTheSame(oldItem: T?, newItem: T?): Boolean {
        return if (oldItem == null && newItem == null) false else oldItem == newItem
    }

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)

    }


}


abstract class BaseDatabindingRecyclerviewAdapter<T>(hasAnimation: Boolean = true) :
    BaseRecyclerViewAdapter<T, BaseRecyclerViewAdapter.DataBindingViewHolder>() {

    open val placeHolderId: Int = R.layout.item_placeholder

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        if (viewType == -1) return DataBindingViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    parent.context
                ), placeHolderId, parent, false
            )
        )
        return DataBindingViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context!!),
                getViewHolderLayoutId(viewType),
                parent,
                false
            )
        )
    }

    final override fun getItemViewType(position: Int): Int {
        return if (shouldShowPlaceholder()) -1
        else getViewType(position)
    }

    override fun getItemCount(): Int {
        return if (shouldShowPlaceholder()) 1 else data?.size ?: 1
    }

    open fun shouldShowPlaceholder(): Boolean {
        return data?.size ?: 0 == 0
    }


    abstract fun getViewHolderLayoutId(viewType: Int): Int

    open fun getViewType(position: Int): Int {
        return 0
    }

}


abstract class BaseRecyclerViewAdapter<T, VH : BaseRecyclerViewAdapter.ViewHolder> :
    RecyclerView.Adapter<VH>() {
    open var data: List<T>? = null
        set(value) {
            field = value
            onDataChanged?.invoke(value)
        }

    var onDataChanged: ((List<T>?) -> Unit)? = null

    open var listener: ((T) -> Unit)? = null

    fun listener(listener: (T) -> Unit) {
        this.listener = listener
    }

    open fun addListener(holder: VH, position: Int) {
        data?.getOrNull(position)?.let { item ->
            holder.itemView.setOnClickListener {
                listener?.invoke(item)
            }
        }
    }


    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        holder.onAppear()
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        holder.onDisappear()
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

    open class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer,
        LifecycleOwner {

        var onAppear: (() -> Unit)? = null
        var onDisappear: (() -> Unit)? = null

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        }

        fun onAppear() {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            onAppear?.invoke()
        }

        fun onDisappear() {
            onDisappear?.invoke()
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
//            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun onRecycled() {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
            onAppear = null
            onDisappear = null
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }
//        val currentRemoveObservers = mutableListOf<(() -> Unit)>()
//        val removeObservers = {
//            for (b in currentRemoveObservers) {
//                b.invoke()
//            }
//            currentRemoveObservers.clear()
//        }
//        var onRecycled: (() -> Unit)? = null
    }

    open class DataBindingViewHolder(val dataBinding: ViewDataBinding) :
        ViewHolder(dataBinding.root) {
        init {
            dataBinding.lifecycleOwner = this
        }


    }

}