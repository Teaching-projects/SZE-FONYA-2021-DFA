//package com.myitsolver.baseandroidapp.util.recyclerview
//
//import androidx.recyclerview.widget.RecyclerView
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.ViewDataBinding
//import com.github.florent37.fiftyshadesof.FiftyShadesOf
//import com.myitsolver.baseandroidapp.util.getItemOrNull
//import com.myitsolver.baseandroidapp.views.biggerThan
//import com.myitsolver.baseandroidapp.views.inflate
//import com.myitsolver.baseandroidapp.views.startLoadingAnimation
//import kotlinx.android.extensions.LayoutContainer
//
//abstract class BaseRecyclerViewAdapter<T>(private val hasPlaceholder: Boolean = true, private val withNotify: Boolean = true, private val perItemNotify: Boolean = true) : RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder>() {
//    var listener: ((T) -> Unit)? = null
//    var listenerWithPos: ((Int, T) -> Unit)? = null
//    var listenerWithView: ((View, T) -> Unit)? = null
//    var longClickListener: ((item: T, pos: Int) -> Boolean)? = null
//    private var animating = true
//    private var loadAnimation: FiftyShadesOf? = null
//
//    var data: MutableList<T>? = mutableListOf()
//        set(value) {
//            val old = field
//            field = value
//            if (animating && (value == null || value.count() == 0)) {
//                animating = false
//                loadAnimation?.stop()
//                notifyItemChanged(0)
//            } else {
//                if (animating) {
//                    animating = false
//                    loadAnimation?.stop()
//                }
//                if ( withNotify) {
//                    if ((old != null && field != null && perItemNotify)) {
//                        val o = old.size
//                        val n = field?.size ?: 0
//
//                        val diff = o - n
//                        var common = if (o < n) o else n
//                        if (common == 0) {
//                            common++
//                        }
//
//                        notifyItemRangeChanged(0, common)
//
//
//                        if (diff > 0) {
//                                notifyItemRangeRemoved(common,diff)
//                        } else if (diff < 0) {
//                            notifyItemRangeInserted(common,diff)
//                        }
//                    } else {
//                        notifyDataSetChanged()
//                    }
//                }
//            }
//        }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (data?.count() ?: 0 > position) {
//            val item = data?.getItemOrNull(position)
//            if (item == null) {
//                onBindViewHolderWithoutData(holder, position)
//            } else {
//                with(holder) {
//                    itemView.setOnClickListener {
//                        listener?.invoke(item)
//                        listenerWithPos?.invoke(position, item)
//                        listenerWithView?.invoke(holder.itemView, item)
//                    }
//                    itemView.setOnLongClickListener {
//                        longClickListener?.invoke(item, position) ?: false
//                    }
//                    onBindViewHolder(holder, position, item)
//                }
//            }
//
//        } else {
//            if (data?.size?.biggerThan(0) == true) {
//                onBindViewHolderWithoutData(holder, position)
//            } else {
//                loadAnimation = onBindPlaceholder(holder)
//                if (animating) {
//                    loadAnimation?.start()
//                } else {
//                    loadAnimation?.stop()
//                }
//            }
//        }
//    }
//
//
//    override fun onViewRecycled(holder: ViewHolder) {
//        super.onViewRecycled(holder)
//        holder.removeObservers()
//        holder.onRecycled?.invoke()
//    }
//    open fun onBindViewHolderWithoutData(holder: ViewHolder, pos: Int) {
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            parent.inflate(
//                getViewHolderLayoutId(viewType)
//            )
//        )
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        if (data == null || data?.count() == 0) {
//            return TYPE_PLACEHOLDER
//        }
//        return super.getItemViewType(position)
//    }
//
//
//    abstract fun getViewHolderLayoutId(viewType: Int): Int
//    abstract fun onBindViewHolder(holder: ViewHolder, position: Int, item: T)
//
//    override fun getItemCount(): Int {
//        data?.size?.let {
//            return if (it > 0) it else if (hasPlaceholder) 1 else 0
//        }
//        return if (hasPlaceholder) 1 else 0
//    }
//
//    open fun onBindPlaceholder(holder: ViewHolder): FiftyShadesOf? {
//        return holder.itemView.startLoadingAnimation()
//    }
//
//    companion object {
//        const val TYPE_PLACEHOLDER = -1
//    }
//
//
//}
//
