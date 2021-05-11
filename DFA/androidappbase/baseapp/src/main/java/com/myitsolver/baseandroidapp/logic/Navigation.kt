package com.myitsolver.baseandroidapp.logic

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.util.Log
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.core.view.ViewCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myitsolver.baseandroidapp.BuildConfig
import com.myitsolver.baseandroidapp.R
import com.myitsolver.baseandroidapp.fragments.BaseFragment
import com.myitsolver.baseandroidapp.util.getItemOrNull
import com.myitsolver.baseandroidapp.views.biggerThan




class Navigation : ViewModel() {
    lateinit var supportFragmentManager: FragmentManager
    val fragments = mutableListOf<BaseFragment>()

    var onFragmentChangedListener: ((old: BaseFragment?, new: BaseFragment?) -> Unit)? = null
    val currentFragment: BaseFragment?
        get() = fragments.getItemOrNull(0)
    val listener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} view created")
            }
        }

        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            super.onFragmentStopped(fm, f)
            (f as? BaseFragment)?.let {
                val ind = fragments.indexOfFirst { it == f }
                onFragmentChangedListener?.invoke(fragments.getItemOrNull(ind), fragments.getItemOrNull(ind + 1))

                fragments.remove(f)
            }
            if (BuildConfig.DEBUG) {

                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} stopped")
            }
        }

        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} created")
            }
        }

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} resumed")
            }
        }

        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
            super.onFragmentAttached(fm, f, context)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} attached")
            }
        }

        override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
            super.onFragmentPreAttached(fm, f, context)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} preAttached")
            }
        }

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentDestroyed(fm, f)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} Destroyed")
            }
        }

        override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
            super.onFragmentSaveInstanceState(fm, f, outState)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} saveInstanceState")
            }
        }

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
            super.onFragmentStarted(fm, f)
            (f as? BaseFragment)?.let {
                fragments.add(0, it)
                onFragmentChangedListener?.invoke(fragments.getItemOrNull(1), fragments.getItemOrNull(0))
            }
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} started")
            }
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentViewDestroyed(fm, f)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} viewDestroyed")
            }
        }

        override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            super.onFragmentPreCreated(fm, f, savedInstanceState)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} preCreated")
            }
        }

        override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            super.onFragmentActivityCreated(fm, f, savedInstanceState)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} activityCreated")
            }
        }


        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            super.onFragmentPaused(fm, f)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} paused")
            }
        }

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
            super.onFragmentDetached(fm, f)
            if (BuildConfig.DEBUG) {
                Log.d("FRAGMENTLIFECYCLE", "${f::class.java} detached")
            }
        }
    }

    init {
//        fragmentReplacement.value = 0
//        fragmentReplacement.observeForever {
//            if (BuildConfig.DEBUG) {
//                Log.d("FRAGMENT_BACKSTACK_SIZE", "$it")
//            }
//        }
    }


    fun resetBackStack() {
        val fm = supportFragmentManager
        for (i in 0..fm.backStackEntryCount) {
            fm.popBackStack()
        }

    }

    fun loadFragment(
        destination: Fragment,
        params: (LoadParams.() -> LoadParams) = {this}
    ): Boolean {

        @Suppress("NAME_SHADOWING") val params = params(LoadParams())

        val shouldBeCleared: Boolean = params.rootFragment ?: (destination as? BaseFragment)?.isRootFragment ?: false
        val canBeAddedAgain: Boolean = params.canBeAddedAgain ?: (destination as? BaseFragment)?.canBeAddedAgain ?: true
        if (!canBeAddedAgain && currentFragment != null && currentFragment!!::class.java == destination::class.java) {
            return false
        }

        if (shouldBeCleared) {
            while (supportFragmentManager.popBackStackImmediate()) {
            }
//            fragmentReplacement.value = 0
        }
        val transaction = supportFragmentManager.beginTransaction()
        if (!shouldBeCleared) {
            params.sharedElements?.let {
                for (v in it) {
                    ViewCompat.getTransitionName(v)?.let {
                        transaction.addSharedElement(v, it)
                    }
                }
            }
        }
        params.animation?.let {
            transaction.setCustomAnimations(
                it.enter,
                it.exit,
                it.popEnter,
                it.popExit
            )
        }
        if (params.hidePrevious) {
            transaction.add(R.id.fragment_container, destination, "${destination::class.java}")
            currentFragment?.let {
                transaction.hide(it)
            }
        } else {
            transaction.replace(R.id.fragment_container, destination, "${destination::class.java}")

        }

        if (params.addToBackStack) {
            transaction.addToBackStack("${destination::class.java}")
//            fragmentReplacement.postValue(fragmentReplacement.value?.plus(1))
        }


        transaction.commitAllowingStateLoss()
        return true
    }

    /**
     * returns true if event is handled
     */
    fun isOnBackPressedHandled(): Boolean {
        if (supportFragmentManager.backStackEntryCount.biggerThan(1)) {
//            fragmentReplacement.value = fragmentReplacement.value?.minus(1)
            val fragment = currentFragment ?: return false
            return fragment.onBackPressed()
        } else {
            return false
        }
    }

    fun shouldHandleOnBackPressed(): Boolean {
        return supportFragmentManager.backStackEntryCount.biggerThan(1)
    }

    fun isUsed(): Boolean {
        return true
    }


    fun getBackstackCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

    inline fun <reified T> positionInStack(cls: Class<T>): Int? {

        for (entry in 0 until supportFragmentManager.backStackEntryCount) {
            val id = supportFragmentManager.getBackStackEntryAt(entry).name
            val f = supportFragmentManager.findFragmentByTag(id)
            if (f is T){
                val back = supportFragmentManager.backStackEntryCount - 1 - entry
                return if (back < 0) null else back
            }
        }

        return null
    }

    class LoadParams {
        var addToBackStack: Boolean = true
        var hidePrevious: Boolean = false
        var animation: LoadAnimation? = null
        var sharedElements: List<View>? = null
        var rootFragment: Boolean? = null
        var canBeAddedAgain: Boolean? = null

        init {
            withAnimation()
        }

        fun noBackStack(): LoadParams {
            addToBackStack = false
            return this
        }

        fun addToBackStack(): LoadParams {
            addToBackStack = true
            return this
        }

        fun keepPrevious(): LoadParams {
            hidePrevious = false
            return this
        }

        fun replacePrevious(): LoadParams {
            hidePrevious = true
            return this
        }

        fun withoutAnimation(): LoadParams {
            animation = null
            return this
        }

        fun withAnimation(
            anim: LoadAnimation = LoadAnimation(
                R.anim.glide_in_f,
                R.anim.glide_out_f,
                R.anim.glide_in,
                R.anim.glide_out
            )
        ): LoadParams {
            animation = anim
            return this
        }

        fun withSharedElements(vararg views: View): LoadParams {
            return withSharedElements(views.asList())
        }

        fun withSharedElements(views: List<View>): LoadParams {
            sharedElements = views
            return this
        }

        /**
         * clear stack
         * With fade animation
         */
        fun asRootFragment(): LoadParams {
            rootFragment = true
            animation = LoadAnimation(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            return this
        }

        fun asNoRootFragment(): LoadParams {
            rootFragment = false
            return this
        }

        fun forceAddAgain(): LoadParams {
            canBeAddedAgain = true
            return this
        }

        fun forceNoAddAgain(): LoadParams {
            canBeAddedAgain = false
            return this
        }

    }

    data class LoadAnimation(
        @AnimatorRes @AnimRes val enter: Int,
        @AnimatorRes @AnimRes val exit: Int,
        @AnimatorRes @AnimRes val popEnter: Int,
        @AnimatorRes @AnimRes val popExit: Int
    )
}
