package com.myitsolver.baseandroidapp.interfaces


import android.view.View
import androidx.fragment.app.Fragment
import com.myitsolver.baseandroidapp.logic.Navigation

/**
 *
 * Created by Patrik on 2016. 10. 31..
 *
 */
interface Controllable {
    fun loadFragment(f: Fragment, params: (Navigation.LoadParams.() -> Navigation.LoadParams)? = null): Boolean
    fun loginRequired()
    fun showLoadingIndicator(show: Boolean)

    fun resetBackStack()
}
