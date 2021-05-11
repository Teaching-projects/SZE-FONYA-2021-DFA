package com.myitsolver.baseandroidapp.binding

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.myitsolver.baseandroidapp.util.textview.boldStyle
import com.myitsolver.baseandroidapp.util.textview.normalStyle
import com.myitsolver.baseandroidapp.views.loadUrl
import com.myitsolver.baseandroidapp.views.loadUrlWithPlaceHolderOrGone
import com.myitsolver.baseandroidapp.views.setMargins
import com.myitsolver.baseandroidapp.views.setPaddings


//@BindingAdapter("android:src")
//fun ImageView.setDrawable(drawable: Drawable?) {
//    setImageDrawable(drawable)
//}

@BindingAdapter(requireAll = false, value = ["imageUrl", "placeholder", "centerCrop"])
fun ImageView.setImageUrl(url: String?, placeholder: Drawable?, centerCrop: Boolean?) {
    loadUrl(url, withCrossfade = false) {
        placeholder?.let {
            placeholder(it)
        }
        if (centerCrop != false){
            centerCrop()
        }else{
            fitCenter()
        }

    }
}

@BindingAdapter("android:backgroundTint")
fun setBackgroundTint(view: View, color: Int) {
    view.backgroundTintList = ColorStateList.valueOf(color)
}

@BindingAdapter("imageBitmap")
fun setImageBitmap(imageView: ImageView, imageBitmap: LiveData<Bitmap>?) {
    if (imageBitmap?.value != null) {
        imageView.setImageBitmap(imageBitmap.value)
    }
}

@BindingAdapter("animateChanges")
fun setAnimateChanges(layout: ConstraintLayout, animate:Boolean?) {
    if (animate == true){
        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }else{
        layout.layoutTransition.disableTransitionType(LayoutTransition.CHANGING)
    }
}

@BindingAdapter("disableParentAnimation")
fun setDisableParentAnimation(layout: ConstraintLayout, animate:Boolean?) {
   layout.layoutTransition.setAnimateParentHierarchy(animate != true)
}

@BindingAdapter("layout_constraintGuide_end")
fun setConstraintGuideEnd(gd: Guideline, value: Float?) {
   gd.setGuidelineEnd((value ?: 0f).toInt())
}

@BindingAdapter("layout_constraintGuide_begin")
fun setConstraintGuideBegin(gd: Guideline, value: Float?) {
   gd.setGuidelineBegin((value ?: 0f).toInt())
}

@BindingAdapter("layout_constraintGuide_percent")
fun setConstraintGuidePercent(gd: Guideline, value: Float?) {
   gd.setGuidelinePercent(value ?: 0f)
}
@BindingAdapter("android:maxLines")
fun TextView.setMaxLinesExt(value: Int) {
   maxLines = value
}

//
//@BindingAdapter("progressAnimated")
//fun CircleProgressView.setProgressAnimated(progress: Int?) {
//    progress?: return
//    this.isIndeterminate = progress == 0
//    this.setProgressInTime(this.progress,progress, 150)
//}

@BindingAdapter("height")
fun setHeight(view: View, height: Float) {
    val layoutParams = view.getLayoutParams()
    layoutParams.height = height.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter(requireAll = false, value = ["android:layout_marginTop", "android:layout_marginRight", "android:layout_marginBottom", "android:layout_marginLeft"])
fun setMargins(view: View,top: Float?, right: Float?, bottom: Float?, left: Float? ) {
   view.setMargins(left?.toInt(), top?.toInt(), right?.toInt(),bottom?.toInt())
}
@BindingAdapter(requireAll = false, value = ["android:paddingTop", "android:paddingRight", "android:paddingBottom", "android:paddingLeft"])
fun setPaddings(view: View,top: Float?, right: Float?, bottom: Float?, left: Float? ) {
   view.setPaddings(left?.toInt(), top?.toInt(), right?.toInt(),bottom?.toInt())
}


@set:BindingAdapter("visibleOrGone")
var View.visibleOrGone
    get() = visibility == VISIBLE
    set(value) {
        visibility = if (value) VISIBLE else GONE
    }

@set:BindingAdapter("visible")
var View.visible
    get() = visibility == VISIBLE
    set(value) {
        visibility = if (value) VISIBLE else INVISIBLE
    }

@BindingAdapter("bold")
fun TextView.setBold(isBold: Boolean?) {
    if (isBold == true){
        boldStyle()
    }else{
        normalStyle()
    }
}


@BindingAdapter("actionSearch")
fun setActionSearch(editText: EditText, block: (()->Unit)?) {
    editText.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            block?.invoke()
            return@setOnEditorActionListener true
        }

        return@setOnEditorActionListener false
    }
}

@BindingAdapter("actionSend")
fun setActionSend(editText: EditText, block: (()->Unit)?) {
    editText.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEND){
            block?.invoke()
            return@setOnEditorActionListener true
        }

        return@setOnEditorActionListener false
    }
}