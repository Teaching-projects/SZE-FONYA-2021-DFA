package com.myitsolver.baseandroidapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.myitsolver.baseandroidapp.R

abstract class FullScreenDialogFragment: DialogFragment(){

    protected abstract val layoutResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyStyle()
    }
    open fun applyStyle(){
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme){
            override fun onBackPressed() {
                if(!this@FullScreenDialogFragment.onBackPressed()){
                    super.onBackPressed()
                }
            }
        }
    }

    open fun onBackPressed(): Boolean{
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResId, container,false)
    }
}

abstract class FullScreenDialogBindingFragment<T : ViewDataBinding>: FullScreenDialogFragment(){

    lateinit var binding: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        onBindingCreated(binding)
        return binding.root
    }

    open fun onBindingCreated(binding: T) {

    }
}

abstract class FullScreenDialogWithoutStatusbarFragment: FullScreenDialogFragment(){
    override fun applyStyle() {
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogWithoutStatusbarStyle)
    }
}

abstract class FullScreenDialogWithoutStatusBarBindingFragment<T : ViewDataBinding>: FullScreenDialogBindingFragment<T>(){
    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogWithoutStatusbarStyle)
    }
}
