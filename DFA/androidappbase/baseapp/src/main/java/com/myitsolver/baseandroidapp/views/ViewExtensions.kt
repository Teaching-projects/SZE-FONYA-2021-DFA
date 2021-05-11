package com.myitsolver.baseandroidapp.views

import android.animation.Animator
import android.animation.LayoutTransition
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.Switch
import androidx.core.view.children
import androidx.core.view.doOnLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.github.florent37.fiftyshadesof.FiftyShadesOf
import com.myitsolver.baseandroidapp.util.U
import java.util.zip.Inflater
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import okhttp3.internal.addHeaderLenient

fun ViewGroup.removeViewWithoutAnimation(v: View){
    val lt = layoutTransition
    lt?.disableTransitionType( LayoutTransition.DISAPPEARING )
    removeView(v)
    lt?.enableTransitionType( LayoutTransition.DISAPPEARING );
}
fun View.removeWithoutAnimation() {
    (parent as? ViewGroup)?.removeViewWithoutAnimation(this)
}

fun ViewGroup.getViewsByTag(tag: String, recursive: Boolean = false): List<View>{
    val views = mutableListOf<View>()
    for ( v in children) {
        if (recursive && v is ViewGroup) {
            views.addAll(v.getViewsByTag(tag,recursive))
        }
        if (v.tag == tag){
            views.add(v)
        }
    }
    return views
}

fun View.setMargins(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val lp = layoutParams as? ViewGroup.MarginLayoutParams
            ?: return

    lp.setMargins(
            left ?: lp.leftMargin,
            top ?: lp.topMargin,
            right ?: lp.rightMargin,
            bottom ?: lp.rightMargin
    )

    layoutParams = lp
}
fun View.setPaddings(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    setPadding(left ?: paddingLeft,top ?: paddingTop,right ?: paddingRight,bottom ?: paddingBottom)
}

fun View.startLoadingAnimation(): FiftyShadesOf {
    return FiftyShadesOf(context).on(this).fadein(true).start()
}

fun View.visibleOrGone(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(layoutId, this, attachToRoot)
}
var factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

fun ImageView.loadUrl(url: String?, withCrossfade: Boolean = false,options: (RequestOptions.() -> RequestOptions) = {centerCrop()}) {
    Glide.with(context).clear(this)
    Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).apply { if(withCrossfade) transition(withCrossFade(factory)) }.apply(options(RequestOptions())).into(this)
}

fun ImageView.loadGlideUrl(url: GlideUrl, withCrossfade: Boolean = false,options: (RequestOptions.() -> RequestOptions) = {centerCrop()}) {
    Glide.with(context).clear(this)
    Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).apply { if(withCrossfade) transition(withCrossFade(factory)) }.apply(options(RequestOptions())).into(this)
}

fun ImageView.loadResource(resId: Int, options: (RequestOptions.() -> RequestOptions) = {centerCrop()}) {
    Glide.with(context).clear(this)
    Glide.with(context).load(resId).apply(options(RequestOptions())).into(this)
}


fun ImageView.loadUrlOrGone(url: String?,withCrossfade: Boolean = false,options: (RequestOptions.() -> RequestOptions) = {centerCrop()}) {
    visibleOrGone(false)
    url?.let {
        if (it.isBlank()) return
        visibleOrGone(true)
        loadUrl(url,withCrossfade, options)
    }
}

fun ImageView.loadUrlWithPlaceHolderOrGone(url: String?, placeholderRes: Int, withCrossfade: Boolean = false,options: (RequestOptions.() -> RequestOptions) = {centerCrop().placeholder(placeholderRes)}) {
    visibleOrGone(false)
    url?.let {
        if (it.contains("rest/image/null")) return
        if (it.isBlank()) return
        visibleOrGone(true)
        loadUrl(url,withCrossfade, options)
    }

}

fun ImageView.loadUrlOrHide(url: String?, withCrossfade: Boolean = false,options: (RequestOptions.() -> RequestOptions) = {centerCrop()}) {
    visibleOrInvisible(false)
    url?.let {
        if (it.isBlank()) return
        visibleOrInvisible(true)
        loadUrl(url,withCrossfade, options)
    }
}

fun ImageView.setSaturation(value: Float) {
    val matrix = ColorMatrix()
    matrix.setSaturation(value)
    val filter = ColorMatrixColorFilter(matrix)
    this.colorFilter = filter
}


/**
 * setImageResource if res is not null and bigger than 0
 */
fun ImageView.setImageResourceOrGone(res: Int?) {
    val visible = res?.biggerThan(0) ?: false
    if (visible) {
        setImageResource(res!!)
    }
    visibleOrGone(visible)
}

/**
 * setImageResource if res is not null and bigger than 0
 */
fun ImageView.setImageResourceOrInvisible(res: Int?) {
    val visible = res?.biggerThan(0) ?: false
    if (visible) {
        setImageResource(res!!)
    }
    visibleOrInvisible(visible)
}

fun Int.biggerThan(value: Int): Boolean {
    return this > value
}

fun onClick(vararg views: View, block: ((View)->Unit)) {
    for (v in views) {
        v.setOnClickListener {
            block(it)
        }
    }
}

fun ImageView.getPixelColorOf(x: Int, y: Int): Int {
    (drawable as? BitmapDrawable)?.bitmap?.let {

        val inverse = Matrix()
        imageMatrix.invert(inverse)
        val touchPoint = floatArrayOf(x.toFloat(), y.toFloat())
        inverse.mapPoints(touchPoint)
        val xCoord = Integer.valueOf(touchPoint[0].toInt())
        val  yCoord = Integer.valueOf(touchPoint[1].toInt())
        if (xCoord >0 && yCoord > 0 && xCoord < it.width && yCoord < it.height)
            return it.getPixel(xCoord,yCoord)
    }
    return -1
}


val View.centerHorizontalInParent
    get() = (left+right)/2
val View.centerVerticalInParent
    get() = (top+bottom)/2
