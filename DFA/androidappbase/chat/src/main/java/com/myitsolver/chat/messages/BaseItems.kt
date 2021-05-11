package com.myitsolver.chat.messages

import androidx.annotation.DrawableRes

open class MessageUser(val id: String, val name: String? = null, val imageUrl: String? = null)

open class BaseMessageItem(val id: String, val user: MessageUser, val message: String, val time: String? = null)

open class BaseChatConfig(
    var showOthersProfileImage: Boolean = true,
    var showOwnProfileImage: Boolean = true,
    var showTime: Boolean = true,
    @DrawableRes var profilePlaceholder: Int? = null)
