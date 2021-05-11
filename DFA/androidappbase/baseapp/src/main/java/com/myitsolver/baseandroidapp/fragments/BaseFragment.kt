package com.myitsolver.baseandroidapp.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.myitsolver.baseandroidapp.activities.BaseActivity
import com.myitsolver.baseandroidapp.activities.FullBaseActivity
import com.myitsolver.baseandroidapp.interfaces.Controllable



interface ActivityResultListener {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}

interface ActivityResult {
    val  onActivityResultListeners :MutableList<ActivityResultListener>
    fun addOnActivityResultListener(listener: ActivityResultListener)
    fun removeOnActivityResultListener(listener: ActivityResultListener)
    val ctx: Context?
    val ctxActivity: Activity?
}

/**
 *
 * Created by Patrik on 2016. 10. 05..
 */
abstract class BaseFragment : Fragment(), ActivityResult {
    /**
     * if true, every other fragment is cleared from the stack
     */
    open val isRootFragment = false
    /**
     * if false, the instance cannot be added to the stack if its class is the same as the current fragment's
     * Note: it doesn't matter if there is an other instance in the stack
     *
     */
    open val canBeAddedAgain = true
    open var hideSoftKeyboardSetup = true

    private var _running = false
    val running
    get() = _running

    override val onActivityResultListeners = mutableListOf<ActivityResultListener>()

    override fun addOnActivityResultListener(listener: ActivityResultListener){
        onActivityResultListeners.add(listener)
    }
    override fun removeOnActivityResultListener(listener: ActivityResultListener){
        onActivityResultListeners.remove(listener)
    }
    override val ctx: Context?
        get() = context
    override val ctxActivity: Activity?
        get() = activity

    private var _visible = false
    val visible
    get() = _visible

    protected abstract val layoutResId: Int

    val controllable: Controllable?
        get() = try {
                activity as Controllable?
            } catch (e: Exception) {
                null
            }


    val fullBaseActivity: FullBaseActivity?
        get() = activity as? FullBaseActivity
    val baseActivity: BaseActivity?
        get() = activity as? BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(layoutResId, container, false)

        return v
    }


    override fun onStart() {
        super.onStart()
        _visible = true
    }

    override fun onStop() {
        super.onStop()
        _visible = false
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        _running = false

    }



    override fun onResume() {
        super.onResume()
        setActionBar()
        if (hideSoftKeyboardSetup) {
            setupUIForHideKeyboard()
        }
        _running = true
        hideSoftKeyboard()
    }

    fun setupUIForHideKeyboard() {
        baseActivity?.setupUI(view!!)
    }

    fun hideSoftKeyboard() {
        baseActivity?.hideSoftKeyboard()
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    open fun setActionBar() {}

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
//        return if (!enter && parentFragment != null) {
//            dummyAnimation
//        } else
           return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResultListeners.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }



    companion object {
        inline fun <FRAGMENT : BaseFragment> FRAGMENT.putArgs(argsBuilder: Bundle.() -> Unit): FRAGMENT = this.apply { arguments = Bundle().apply(argsBuilder) }

        //    SUPPORT NESTED ANIMATIONS
//        private val dummyAnimation = AlphaAnimation(1f, 1f)
//
//        initialize {
//            dummyAnimation.duration = 500
//        }
    }



}


abstract class BaseBindingFragment<T : ViewDataBinding> : BaseFragment() {

    lateinit var binding: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        onBindingCreated(binding)
        return binding.root
    }

    open fun onBindingCreated(binding: T) {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::binding.isInitialized) {
            binding.unbind()
        }
    }
}