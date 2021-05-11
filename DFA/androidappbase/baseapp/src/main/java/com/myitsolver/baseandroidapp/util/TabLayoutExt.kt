package com.myitsolver.baseandroidapp.util

import com.google.android.material.tabs.TabLayout

fun TabLayout.onTabSelected(listener: ((TabLayout.Tab)->Unit)){
    addOnTabSelectedListener(object: TabLayout.BaseOnTabSelectedListener<TabLayout.Tab?> {
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            listener(p0 ?: return)
        }
    })
}
fun TabLayout.onTabUnselected(listener: ((TabLayout.Tab)->Unit)){
    addOnTabSelectedListener(object: TabLayout.BaseOnTabSelectedListener<TabLayout.Tab?> {
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
            listener(p0 ?: return)

        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
        }
    })
}
fun TabLayout.onTabReselected(listener: ((TabLayout.Tab)->Unit)){
    addOnTabSelectedListener(object: TabLayout.BaseOnTabSelectedListener<TabLayout.Tab?> {
        override fun onTabReselected(p0: TabLayout.Tab?) {
            listener(p0 ?: return)
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
        }
    })
}
