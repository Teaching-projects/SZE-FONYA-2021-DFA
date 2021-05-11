package com.myitsolver.baseandroidapp.util.textview

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.myitsolver.baseandroidapp.util.U
import me.saket.bettermovementmethod.BetterLinkMovementMethod


fun TextView.clickableText(block: (ClickableTextBuilderWithContext.()->Unit)) {
    val builder = ClickableTextBuilderWithContext(this)
    block(builder)
}

class ClickableTextBuilderWithContext(private val textView: TextView): ClickableTextBuilder() {

    fun text(@StringRes res: Int) {
        text(textView.context.getString(res))
    }

    fun clickable(@StringRes res: Int, action: String) {
        super.clickable(textView.context.getString(res), action)
    }
    fun linkColor(@ColorRes res: Int) {
        textView.setLinkTextColor(textView.resources.getColor(res))
    }
    fun build(listener: ((action:String)->Unit)) {
        textView.text = U.fromHtml(str)
        BetterLinkMovementMethod.linkifyHtml(textView).setOnLinkClickListener { _, url ->
            listener(url)
            true
        }
    }



}

open class ClickableTextBuilder{
    var str = ""
    fun text(text: String) {
        str += text
    }
    open fun clickable(text: String, action:String){
        str += "<a href=\"$action\">$text</a>"
    }



}

