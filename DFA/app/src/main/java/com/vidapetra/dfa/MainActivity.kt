package com.vidapetra.dfa

import android.os.Bundle
import com.myitsolver.baseandroidapp.activities.BaseActivity
import com.myitsolver.baseandroidapp.activities.FullBaseActivity
import com.vidapetra.dfa.logic.Singletons.activityContext
import com.vidapetra.dfa.ui.DfaListFragment

class MainActivity : FullBaseActivity() {

    override var config = Config(false,false,false,true)
    override val contentView = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadFragment(DfaListFragment())

    }

    override fun onStart() {
        super.onStart()
        activityContext = this
    }
    override fun onStop() {
        super.onStop()
        activityContext = null
    }
}