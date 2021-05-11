package com.myitsolver.baseandroidapp.views

import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import com.myitsolver.baseandroidapp.util.getItemOrNull

class PopupMenuItems {
    val items = mutableListOf<PopupMenuItem>()
    fun item(
        title: String?,
        group: Int = 0,
        order: Int = Menu.NONE,
        onClick: (() -> Unit)
    ) {

        items.add(
            PopupMenuItem(
                title ?: "",
                group,
                order,
                onClick
            )
        )
    }

}

data class PopupMenuItem(
    val title: String,
    val group: Int = 0,
    val order: Int = Menu.NONE,
    val onClick: (() -> Unit)
)

inline fun View.showPopupMenuOnClick(crossinline addItems:  PopupMenuItems.() -> Unit) {
    setOnClickListener {
        showPopupMenu(addItems)
    }
}

inline fun View.showPopupMenu(addItems: PopupMenuItems.() -> Unit): PopupMenu {
    return PopupMenu(context, this).apply {
        PopupMenuItems().apply {
            addItems()
            items.forEachIndexed { index, p ->
                menu.add(p.group, index, p.order, p.title)
            }
            setOnMenuItemClickListener {
                items.getItemOrNull(it.itemId)?.onClick?.invoke()
                true
            }
            show()
        }
    }
}