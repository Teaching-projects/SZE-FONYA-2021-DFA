package com.myitsolver.baseandroidapp.views


import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TabWidget
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.core.view.isVisible

class BadgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle,
    target: View? = null,
    tabIndex: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {


    /**
     * Returns the target View this badge has been attached to.
     *
     */
    var target: View? = null
        private set

    /**
     * Returns the positioning of this badge.
     *
     * one of POSITION_TOP_LEFT, POSITION_TOP_RIGHT, POSITION_BOTTOM_LEFT, POSITION_BOTTOM_RIGHT, POSTION_CENTER.
     *
     */
    /**
     * Set the positioning of this badge.
     *
     * @param layoutPosition one of POSITION_TOP_LEFT, POSITION_TOP_RIGHT, POSITION_BOTTOM_LEFT, POSITION_BOTTOM_RIGHT, POSTION_CENTER.
     */
    var badgePosition: Int = 0
    /**
     * Returns the horizontal margin from the target View that is applied to this badge.
     *
     */
    var horizontalBadgeMargin: Int = 0
        private set
    /**
     * Returns the vertical margin from the target View that is applied to this badge.
     *
     */
    var verticalBadgeMargin: Int = 0
        private set
    private var badgeColor: Int = 0

    private var isShown: Boolean = false

    private var badgeBg: ShapeDrawable? = null

    private var targetTabIndex: Int = 0

    private val defaultBackground: ShapeDrawable
        get() {

            val r = dipToPixels(DEFAULT_CORNER_RADIUS_DIP)
            val outerR = floatArrayOf(
                r.toFloat(),
                r.toFloat(),
                r.toFloat(),
                r.toFloat(),
                r.toFloat(),
                r.toFloat(),
                r.toFloat(),
                r.toFloat()
            )

            val rr = RoundRectShape(outerR, null, null)
            val drawable = ShapeDrawable(rr)
            drawable.paint.color = badgeColor

            return drawable

        }

    /**
     * Returns the color value of the badge background.
     *
     */
    /**
     * Set the color value of the badge background.
     *
     * @param badgeColor the badge background color.
     */
    var badgeBackgroundColor: Int
        get() = badgeColor
        set(badgeColor) {
            this.badgeColor = badgeColor
            badgeBg = defaultBackground
        }

    /**
     * Constructor -
     *
     * create a new BadgeView instance attached to a target [android.view.View].
     *
     * @param context context for this view.
     * @param target the View to attach the badge to.
     */
    constructor(context: Context, target: View) : this(context, null, android.R.attr.textViewStyle, target, 0)

    /**
     * Constructor -
     *
     * create a new BadgeView instance attached to a target [android.widget.TabWidget]
     * tab at a given index.
     *
     * @param context context for this view.
     * @param target the TabWidget to attach the badge to.
     * @param index the position of the tab within the target.
     */
    constructor(context: Context, target: TabWidget, index: Int) : this(
        context,
        null,
        android.R.attr.textViewStyle,
        target,
        index
    )

    init {
        init(target, tabIndex)
    }

    private fun init(target: View?, tabIndex: Int) {


        this.target = target
        this.targetTabIndex = tabIndex

        // apply defaults
        badgePosition = DEFAULT_POSITION
        horizontalBadgeMargin = dipToPixels(DEFAULT_MARGIN_DIP)
        verticalBadgeMargin = horizontalBadgeMargin
        badgeColor = DEFAULT_BADGE_COLOR

        typeface = Typeface.DEFAULT_BOLD
        val paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP)
        setPadding(paddingPixels, 0, paddingPixels, 0)
        setTextColor(DEFAULT_TEXT_COLOR)

        fadeIn = AlphaAnimation(0f, 1f)
        fadeIn!!.interpolator = DecelerateInterpolator()
        fadeIn!!.duration = 200

        fadeOut = AlphaAnimation(1f, 0f)
        fadeOut!!.interpolator = AccelerateInterpolator()
        fadeOut!!.duration = 200

        setTextSize(12.0f)

        isShown = false

        if (this.target != null) {
            applyTo(this.target!!)
        } else {
            show()
        }

    }


    private fun applyTo(target: View) {
        @Suppress("NAME_SHADOWING")
        var target = target

        val lp = target.layoutParams
        val parent = target.parent
        val container = FrameLayout(context!!)
        container.tag = BADGE_HOLDER
        this.elevation = target.elevation + 1
        if (target is TabWidget) {

            // set target to the relevant tab child container
            target = target.getChildTabViewAt(targetTabIndex)
            this.target = target

            (target as ViewGroup).addView(
                container,
                LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)
            )
            this.tag = BADGE

            this.visibility = View.GONE
            container.addView(this)

        } else if (parent is ConstraintLayout) {
            var testId = 0
            var found = false
            do {
                testId++
                found = parent.children.filter { it.id == testId }.any()

            } while (found)
            id = testId
            parent.addView(this)

            this.tag = target.id.toString()
        } else if (parent is ViewGroup) {
            val index = parent.indexOfChild(target)

            parent.removeView(target)
            parent.addView(container, index, lp)

            container.addView(target)

            this.visibility = View.GONE
            target.layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            container.addView(this)
            this.tag = BADGE
            parent.invalidate()

        }

    }

    /**
     * Make the badge visible in the UI.
     *
     */
    fun show() {
        show(false, null)
    }

    /**
     * Make the badge visible in the UI.
     *
     * @param animate flag to apply the default fade-in animation.
     */
    fun show(animate: Boolean) {
        if (!isShown) {
            show(animate, fadeIn)
        }
    }

    /**
     * Make the badge visible in the UI.
     *
     * @param anim Animation to apply to the view when made visible.
     */
    fun show(anim: Animation) {
        show(true, anim)
    }

    /**
     * Make the badge non-visible in the UI.
     *
     */
    fun hide() {
        hide(false, null)
    }

    /**
     * Make the badge non-visible in the UI.
     *
     * @param animate flag to apply the default fade-out animation.
     */
    fun hide(animate: Boolean) {
        hide(animate, fadeOut)
    }

    /**
     * Make the badge non-visible in the UI.
     *
     * @param anim Animation to apply to the view when made non-visible.
     */
    fun hide(anim: Animation) {
        hide(true, anim)
    }

    /**
     * Toggle the badge visibility in the UI.
     *
     */
    fun toggle() {
        toggle(false, null, null)
    }

    /**
     * Toggle the badge visibility in the UI.
     *
     * @param animate flag to apply the default fade-in/out animation.
     */
    fun toggle(animate: Boolean) {
        toggle(animate, fadeIn, fadeOut)
    }

    /**
     * Toggle the badge visibility in the UI.
     *
     * @param animIn Animation to apply to the view when made visible.
     * @param animOut Animation to apply to the view when made non-visible.
     */
    fun toggle(animIn: Animation, animOut: Animation) {
        toggle(true, animIn, animOut)
    }

    private fun show(animate: Boolean, anim: Animation?) {
        if (background == null) {
            if (badgeBg == null) {
                badgeBg = defaultBackground
            }
            setBackgroundDrawable(badgeBg)
        }
        applyLayoutParams()

        if (animate) {
            this.startAnimation(anim)
        }
        this.visibility = View.VISIBLE
        isShown = true
    }

    private fun hide(animate: Boolean, anim: Animation?) {
        this.visibility = View.GONE
        if (animate) {
            this.startAnimation(anim)
        }
        isShown = false
    }

    private fun toggle(animate: Boolean, animIn: Animation?, animOut: Animation?) {
        if (isShown) {
            hide(animate && animOut != null, animOut)
        } else {
            show(animate && animIn != null, animIn)
        }
    }

    /**
     * Increment the numeric badge label. If the current badge label cannot be converted to
     * an integer value, its label will be set to "0".
     *
     * @param offset the increment offset.
     */
    fun increment(offset: Int): Int {
        val txt = text
        var i: Int
        if (txt != null) {
            try {
                i = Integer.parseInt(txt.toString())
            } catch (e: NumberFormatException) {
                i = 0
            }

        } else {
            i = 0
        }
        i = i + offset
        text = i.toString()
        return i
    }

    /**
     * Decrement the numeric badge label. If the current badge label cannot be converted to
     * an integer value, its label will be set to "0".
     *
     * @param offset the decrement offset.
     */
    fun decrement(offset: Int): Int {
        return increment(-offset)
    }

    private fun applyLayoutParams() {
        val p = parent
        if (p is FrameLayout) {
            val lp = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

            when (badgePosition) {
                POSITION_TOP_LEFT -> {
                    lp.gravity = Gravity.LEFT or Gravity.TOP
                    lp.setMargins(horizontalBadgeMargin, verticalBadgeMargin, 0, 0)
                }
                POSITION_TOP_RIGHT -> {
                    lp.gravity = Gravity.RIGHT or Gravity.TOP
                    lp.setMargins(0, verticalBadgeMargin, horizontalBadgeMargin, 0)
                }
                POSITION_BOTTOM_LEFT -> {
                    lp.gravity = Gravity.LEFT or Gravity.BOTTOM
                    lp.setMargins(horizontalBadgeMargin, 0, 0, verticalBadgeMargin)
                }
                POSITION_BOTTOM_RIGHT -> {
                    lp.gravity = Gravity.RIGHT or Gravity.BOTTOM
                    lp.setMargins(0, 0, horizontalBadgeMargin, verticalBadgeMargin)
                }
                POSITION_CENTER -> {
                    lp.gravity = Gravity.CENTER
                    lp.setMargins(0, 0, 0, 0)
                }
                else -> {
                }
            }

            layoutParams = lp
        } else if (p is ConstraintLayout) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(p)
            target?.let { target ->
                constraintSet.connect(id, ConstraintSet.TOP, target.id, ConstraintSet.TOP, verticalBadgeMargin)
                constraintSet.connect(id, ConstraintSet.BOTTOM, target.id, ConstraintSet.BOTTOM, verticalBadgeMargin)
                constraintSet.connect(id, ConstraintSet.RIGHT, target.id, ConstraintSet.RIGHT, horizontalBadgeMargin)
                constraintSet.connect(id, ConstraintSet.LEFT, target.id, ConstraintSet.LEFT, horizontalBadgeMargin)
            }
            when (badgePosition) {
                POSITION_TOP_LEFT -> {

                    constraintSet.setHorizontalBias(id, 0f)
                    constraintSet.setVerticalBias(id, 0f)

                }
                POSITION_TOP_RIGHT -> {
                    constraintSet.setHorizontalBias(id, 1f)
                    constraintSet.setVerticalBias(id, 0f)

                }
                POSITION_BOTTOM_LEFT -> {
                    constraintSet.setHorizontalBias(id, 0f)
                    constraintSet.setVerticalBias(id, 1f)

                }
                POSITION_BOTTOM_RIGHT -> {
                    constraintSet.setHorizontalBias(id, 1f)
                    constraintSet.setVerticalBias(id, 1f)

                }
                POSITION_CENTER -> {
                    constraintSet.setHorizontalBias(id, 0.5f)
                    constraintSet.setVerticalBias(id, 0.5f)


                }
                else -> {
                }
            }

            constraintSet.applyTo(p)
        }

    }

    /**
     * Is this badge currently visible in the UI?
     *
     */
    override fun isShown(): Boolean {
        return isShown
    }

    /**
     * Set the horizontal/vertical margin from the target View that is applied to this badge.
     *
     * @param badgeMargin the margin in pixels.
     */
    fun setBadgeMargin(badgeMargin: Int) {
        this.horizontalBadgeMargin = badgeMargin
        this.verticalBadgeMargin = badgeMargin
    }

    /**
     * Set the horizontal/vertical margin from the target View that is applied to this badge.
     *
     * @param horizontal margin in pixels.
     * @param vertical margin in pixels.
     */
    fun setBadgeMargin(horizontal: Int, vertical: Int) {
        this.horizontalBadgeMargin = horizontal
        this.verticalBadgeMargin = vertical
    }

    private fun dipToPixels(dip: Int): Int {
        val r = resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), r.displayMetrics)
        return px.toInt()
    }

    companion object {

        val POSITION_TOP_LEFT = 1
        val POSITION_TOP_RIGHT = 2
        val POSITION_BOTTOM_LEFT = 3
        val POSITION_BOTTOM_RIGHT = 4
        val POSITION_CENTER = 5

        private val DEFAULT_MARGIN_DIP = 5
        private val DEFAULT_LR_PADDING_DIP = 5
        private val DEFAULT_CORNER_RADIUS_DIP = 10
        private val DEFAULT_POSITION = POSITION_TOP_RIGHT
        private val DEFAULT_BADGE_COLOR = Color.parseColor("#FF9F88") //Color.RED;
        private val DEFAULT_TEXT_COLOR = Color.WHITE

        private var fadeIn: Animation? = null
        private var fadeOut: Animation? = null

        val BADGE_HOLDER = "badgeHolder"
        val BADGE = "badge"
    }


}

fun View.showNumberedBadge(value: Int?) {
    if (value == null || value == 0) {
        hideBadge()
    } else {
        showBadge("$value")
    }
}


fun View.showBadge(text: String, animate: Boolean = true, gravity: Int = BadgeView.POSITION_TOP_RIGHT) {
    var b = badge
    if (b == null) {
        b = BadgeView(context, this)
    }
    b.badgePosition = gravity
    b.text = text
    b.show(animate)
}

fun View.hideBadge(animate: Boolean = true) {
    if (badge?.isVisible == true) {
        badge?.hide(animate)
    }
}

val View.badge: BadgeView?
    get() {

        val p = parent

        if (this is FrameLayout && tag == BadgeView.BADGE_HOLDER) {
            return children.filter { it.tag == BadgeView.BADGE }.firstOrNull() as? BadgeView
        } else if (p is FrameLayout) {
            if (p.tag == BadgeView.BADGE_HOLDER) {
                return p.children.filter { it.tag == BadgeView.BADGE }.firstOrNull() as? BadgeView
            }
        } else if (p is ConstraintLayout) {
            return p.children.filter { it.tag == "$id" }.firstOrNull() as? BadgeView
        }
        return null
    }