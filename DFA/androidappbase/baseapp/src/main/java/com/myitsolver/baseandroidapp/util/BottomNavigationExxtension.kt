package com.myitsolver.baseandroidapp.util


import android.annotation.SuppressLint
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
import android.R.attr.gravity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.myitsolver.baseandroidapp.R


@SuppressLint("RestrictedApi")
fun BottomNavigationView.removeShiftMode() {
//    val menuView = this.getChildAt(0) as BottomNavigationMenuView
//    try {
//        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
//        shiftingMode.isAccessible = true
//        shiftingMode.setBoolean(menuView, false)
//        shiftingMode.isAccessible = false
//        for (i in 0 until menuView.childCount) {
//            val item = menuView.getChildAt(i) as BottomNavigationItemView
////                throw Exception("mShiftingMode is no longer available: BottomNavigationViewHelper")
//            item.setShifting(false)
////            // set once again checked value, so view will be updated
////
//            item.setChecked(item.itemData.isChecked)
//        }
//    } catch (e: NoSuchFieldException) {
//        Log.e("BottomNav", "Unable to get shift mode field", e)
//    } catch (e: IllegalAccessException) {
//        Log.e("BottomNav", "Unable to change value of shift mode", e)
//    }

}

private fun BottomNavigationView.addBadge( position: Int, num: Int) {

        val bottomMenu = getChildAt(0) as BottomNavigationMenuView
        val v = bottomMenu.getChildAt(position) as? BottomNavigationItemView
        if (v != null) {
            var badge: View? = null
            for (i in 0 until v.childCount) {
                val o = v.getChildAt(i).tag
                if (o != null && o is String && o == "badge") {
                    badge = v.getChildAt(i)
                    break
                }
            }
             if (badge == null) {
                 badge = LayoutInflater.from(context).inflate(R.layout.badge_layout, bottomMenu, false)
             }
            badge = badge ?: return
            var text = ""
            if (num > 0) {
                text = "" + num
            }
            (badge.findViewById(R.id.number) as AppCompatTextView).text = text
            val badgeLayout = FrameLayout.LayoutParams(badge.layoutParams)
            badgeLayout.gravity = Gravity.CENTER_HORIZONTAL
            badgeLayout.topMargin = U.dpToPx(context, 6)
            badgeLayout.leftMargin = U.dpToPx(context, 20)
            badge.tag = "badge"
            v.addView(badge, badgeLayout)
        }
}