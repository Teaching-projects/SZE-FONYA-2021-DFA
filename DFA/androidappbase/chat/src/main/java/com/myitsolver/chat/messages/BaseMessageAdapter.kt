package com.myitsolver.chat.messages

import android.widget.ImageView
import android.widget.TextView
import com.myitsolver.baseandroidapp.logic.EventBus
import com.myitsolver.baseandroidapp.util.getItemOrNull
import com.myitsolver.baseandroidapp.util.recyclerview.BaseDiffRecyclerviewAdapter
import com.myitsolver.baseandroidapp.views.loadUrl
import com.myitsolver.baseandroidapp.views.visibleOrGone
import com.myitsolver.chat.R
import me.carleslc.kotlin.extensions.standard.with

open class BaseMessageAdapter : BaseDiffRecyclerviewAdapter<BaseMessageItem>() {



    var currentUserId: String? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    open var baseChatConfig = BaseChatConfig()


    override fun getViewHolderLayoutId(viewType: Int): Int {
        return when (viewType) {
            MSG_TEXT_OTHER -> R.layout.msg_other
            MSG_TEXT_OWN -> R.layout.msg_own
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, viewType: Int) {
        if(position == 0){
            chatEventBus.shouldLoadPrevMessages.postValue(true)
        }
        when (viewType) {
            MSG_TEXT_OTHER -> bindOtherTextMessage(holder, data?.getItemOrNull(position) ?: return)
            MSG_TEXT_OWN -> bindOwnTextMessage(holder, data?.getItemOrNull(position) ?: return)
            else -> {}
        }

    }

    open fun bindOwnTextMessage(holder: ViewHolder, baseMessageItem: BaseMessageItem) {
        bindTextMessage(holder, baseMessageItem)
        with(holder.itemView) {
            findViewById<ImageView>(R.id.ivProfile).apply {
               visibleOrGone(baseChatConfig.showOwnProfileImage)
            }
        }
    }

    open fun bindOtherTextMessage(holder: ViewHolder, baseMessageItem: BaseMessageItem) {
        bindTextMessage(holder, baseMessageItem)
        with(holder.itemView) {
            findViewById<ImageView>(R.id.ivProfile).apply {
                visibleOrGone(baseChatConfig.showOthersProfileImage)
            }
        }
    }

    open fun bindTextMessage(holder: ViewHolder, baseMessageItem: BaseMessageItem) {
        bindMessage(holder, baseMessageItem)
        with(holder.itemView) {
            findViewById<TextView>(R.id.tvMessage).apply {
                text = baseMessageItem.message
            }
            setOnClickListener {
                chatEventBus.messageClickedEvent.postValue(baseMessageItem)
            }
            setOnLongClickListener {
                chatEventBus.messageLongClickedEvent.postValue(baseMessageItem)
                true
            }
        }
    }

    open fun bindMessage(holder: ViewHolder, baseMessageItem: BaseMessageItem) {
        with(holder.itemView) {
            findViewById<TextView>(R.id.tvTime).apply {
                text = baseMessageItem.time
                visibleOrGone(baseMessageItem.time != null && baseChatConfig.showTime)
            }
            findViewById<ImageView>(R.id.ivProfile).apply {
                loadUrl(baseMessageItem.user.imageUrl) {
                    centerCrop()
                    placeholder(baseChatConfig.profilePlaceholder ?: R.drawable.placeholder)
                }
                setOnClickListener {
                    chatEventBus.profileClickedEvent.postValue(baseMessageItem.user)
                }
                setOnLongClickListener {
                    chatEventBus.profileLongClickedEvent.postValue(baseMessageItem.user)
                    true
                }
            }
        }
    }

    override fun getViewType(position: Int): Int {
        return if (data?.getItemOrNull(position)?.user?.id == currentUserId) {
            MSG_TEXT_OWN
        } else {
            MSG_TEXT_OTHER
        }
    }

    companion object {
        const val MSG_TEXT_OWN = 1
        const val MSG_TEXT_OTHER = 2

    }

}

class ChatEventBus : EventBus()

val chatEventBus = ChatEventBus()

val ChatEventBus.profileClickedEvent
    get() = chatEventBus.getBus("ProfileClicked")
val ChatEventBus.profileLongClickedEvent
    get() = chatEventBus.getBus("ProfileLongClicked")
val ChatEventBus.messageClickedEvent
    get() = chatEventBus.getBus("MessageClicked")
val ChatEventBus.messageLongClickedEvent
    get() = chatEventBus.getBus("MessageLongClicked")
val ChatEventBus.shouldLoadPrevMessages
    get() = chatEventBus.getBus("ShouldLoadPrevMessages")