package com.myitsolver.baseandroidapp.logic

import android.view.Gravity
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout

class DrawerSynchronizer(private val drawerLayout: DrawerLayout, autoLock: Boolean) {

    var onLeftOpenedListener: ((open: Boolean) -> Unit)? = null
    var onRightOpenedListener: ((open: Boolean) -> Unit)? = null

    init {
        drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {
                if (autoLock) {
                    when ((drawerView.layoutParams as? DrawerLayout.LayoutParams)?.gravity) {
                        Gravity.START -> {
                            onLeftOpenedListener?.invoke(false)
                            lockRightClosed(false)
                        }
                        Gravity.END -> {
                            onRightOpenedListener?.invoke(false)
                            lockLeftClosed(false)
                        }
                    }
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                if (autoLock) {
                    when ((drawerView.layoutParams as? DrawerLayout.LayoutParams)?.gravity) {
                        Gravity.START -> {
                            onLeftOpenedListener?.invoke(true)
                            lockRightClosed(true)
                        }
                        Gravity.END -> {
                            onRightOpenedListener?.invoke(true)
                            lockLeftClosed(true)
                        }
                    }
                }
            }
        })
    }

    fun openLeft(){
        drawerLayout.closeDrawers()
        drawerLayout.openDrawer(Gravity.START)
    }

    fun openRight(){
        drawerLayout.closeDrawers()
        drawerLayout.openDrawer(Gravity.END)

    }

    fun closeLeft(){
        drawerLayout.closeDrawer(Gravity.START)
    }

    fun closeRight(){
        drawerLayout.closeDrawer(Gravity.END)

    }

    fun lockLeftClosed(lock: Boolean){
        drawerLayout.setDrawerLockMode(if (lock)DrawerLayout.LOCK_MODE_LOCKED_CLOSED else DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.START)
    }
    fun lockRightClosed(lock: Boolean){
        drawerLayout.setDrawerLockMode(if (lock)DrawerLayout.LOCK_MODE_LOCKED_CLOSED else DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.END)
    }

}

fun DrawerLayout.addSlideListener(listener: ((drawerView: View, slideOffset: Float)->Unit)) {
    addDrawerListener(object: DrawerLayout.DrawerListener {
        override fun onDrawerStateChanged(newState: Int) {}

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            listener.invoke(drawerView,slideOffset)
        }

        override fun onDrawerClosed(drawerView: View) {}

        override fun onDrawerOpened(drawerView: View) {}
    })
}

fun DrawerLayout.addDrawerStateListener(listener: ((newState: Int)->Unit)) {
    addDrawerListener(object: DrawerLayout.DrawerListener {
        override fun onDrawerStateChanged(newState: Int) {
            listener.invoke(newState)
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        }

        override fun onDrawerClosed(drawerView: View) {}

        override fun onDrawerOpened(drawerView: View) {}
    })
}