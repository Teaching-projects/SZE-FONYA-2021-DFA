@file:Suppress("unused", "RedundantOverride")

package com.myitsolver.baseandroidapp.activities

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import android.widget.*
import com.myitsolver.baseandroidapp.R


import com.myitsolver.baseandroidapp.interfaces.Controllable
import com.myitsolver.baseandroidapp.interfaces.IndicatorShower
import com.myitsolver.baseandroidapp.util.removeShiftMode
import com.myitsolver.baseandroidapp.views.old.BaseNavigationDrawerView
import com.myitsolver.baseandroidapp.views.visibleOrGone

import kotlinx.android.synthetic.main.activity_base.*


/**
 *
 * Created by Patrik on 2015. 11. 02..
 */
abstract class FullBaseActivity : BaseActivity(), IndicatorShower, Controllable, BottomNavigationView.OnNavigationItemSelectedListener, BaseNavigationDrawerView.OnNavigationDrawerItemClickListener {

    protected var actionBarMenu: Menu? = null
    private var fragmentReplacement = 0
    private var lastBackTime = System.currentTimeMillis()

    protected open var navigationDrawerView: BaseNavigationDrawerView? = null
       set(value) {
           field = value
           updateNavigationDrawerView()
       }

    protected var mDrawerToggle: ActionBarDrawerToggle? = null

    protected override val contentView: Int
        get() = R.layout.activity_base

    open protected val bottomBarMenu: Int
        get() = R.menu.menu_empty_bottom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        navigationDrawerView = object : BaseNavigationDrawerView(context) {
//
//            override fun getLayoutId(): Int {
//                return R.layout.view_navigation_drawer
//            }
//        }



        if (config.hasToolbar) {
            setSupportActionBar(toolbar)
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }

        setLoadingMarks()
        //        showProgressBar();
        if (config.hasBottomMenu) {
            initBottomNavigation()
        } else {
            bottom_navigation?.visibility = View.GONE
        }
        if (config.hasNavDrawer) {

            updateNavigationDrawerView()
            initHamburgerIcon()
        } else {
            drawer_layout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    protected fun updateNavigationDrawerView() {
        if (navigationDrawerView != null) {
            layoutDrawer?.removeAllViews()
            layoutDrawer?.addView(navigationDrawerView,LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
            layoutDrawer?.invalidate()
            layoutDrawer?.requestLayout()

        }
        initNavDrawer()

    }


    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //VIEWS

    @Suppress("DEPRECATION")
    private fun initHamburgerIcon() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mDrawerToggle = object : ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.app_name, R.string.app_name) {

            override fun onDrawerClosed(view: View) {
                supportInvalidateOptionsMenu()
                //drawerOpened = false;
            }

            override fun onDrawerOpened(drawerView: View) {
                supportInvalidateOptionsMenu()
                //drawerOpened = true;
            }
        }


        mDrawerToggle?.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(mDrawerToggle!!)
        mDrawerToggle?.syncState()

    }

    private fun initNavDrawer() {
        navigationDrawerView?.setListener(this)
    }

    override fun onNavigationDrawerItemSelected(id: Int): Boolean {
        drawer_layout.closeDrawer(layoutDrawer)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle?.syncState()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle?.onConfigurationChanged(newConfig)

    }

    private fun initBottomNavigation() {
        bottom_navigation?.setOnNavigationItemSelectedListener(this)
        bottom_navigation?.inflateMenu(bottomBarMenu)
        bottom_navigation?.removeShiftMode()
    }

    fun setBottomNavigationVisible(visible: Boolean) {
        bottom_navigation?.visibleOrGone(visible)
    }


    fun setActionBarVisible(visible: Boolean) {
        if (visible) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    override fun onBackPressed() {
        if (config.hasNavDrawer && (drawer_layout.isDrawerOpen(Gravity.RIGHT) || drawer_layout.isDrawerOpen(Gravity.LEFT))){
            drawer_layout.closeDrawers()
        }else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    fun setActionBarColor(colorRes: Int) {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(colorRes)));
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

}
