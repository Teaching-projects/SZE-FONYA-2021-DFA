package com.myitsolver.baseandroidapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.app.DialogCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.myitsolver.baseandroidapp.R
import com.myitsolver.baseandroidapp.util.dpToPx


open class RoundedDialogFragment: DialogFragment() {


    override fun getTheme(): Int = R.style.DialogTheme


    override fun onCreateDialog(savedInstanceState: Bundle?)= object : Dialog(activity!!, theme){
        override fun onBackPressed() {
            if(!this@RoundedDialogFragment.onBackPressed()){
                super.onBackPressed()
            }
        }
    }.apply {
        window?.apply {
            setBackgroundDrawable(InsetDrawable(resources.getDrawable(R.drawable.bg_dialog),dpToPx(16)))
        }
    }


    open fun onBackPressed(): Boolean{
        return false
    }

}