package com.myitsolver.baseandroidapp.views

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet

import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.myitsolver.baseandroidapp.R
import kotlinx.android.synthetic.main.view_image_slider.view.*

// import butterknife.BindView;

/**
 * Created by Peter on 2018. 01. 18..
 */

class ImageSlider : BaseView, BaseSliderView.OnSliderClickListener {

    @Suppress("MemberVisibilityCanBePrivate")
    var itemClickListener: ((Int) -> Unit)? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun getLayoutId() = R.layout.view_image_slider


    init {
        slider.setCustomIndicator(slider_indicator)
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion)
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        slider.setCustomAnimation(DescriptionAnimation())
        slider.setDuration(4000)
    }

    fun setSliderListener(listener: ViewPagerEx.OnPageChangeListener) {
        slider.addOnPageChangeListener(listener)
    }

    fun stopAutoCycle(){
        slider.stopAutoCycle()
    }

    fun startAutoCycle(){
        slider.startAutoCycle()
    }

    fun addData(id: Int, name: String, url: String) {
        val textSliderView = TextSliderView(context)

        textSliderView
                .description(name)
                .image(url)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .setOnSliderClickListener(this)
        textSliderView.bundle(Bundle())
        textSliderView.bundle
                .putInt("id", id)

        slider.addSlider(textSliderView)

    }

    fun addData(id: Int, name: String, url: Int) {
        val textSliderView = TextSliderView(context)

        textSliderView
                .description(name)
                .image(url)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(this)

        textSliderView.bundle(Bundle())
        textSliderView.bundle
                .putInt("id", id)

        slider.addSlider(textSliderView)

    }

    override fun onSliderClick(slider: BaseSliderView) {
        itemClickListener?.invoke(slider.bundle.getInt("id"))

    }

}
