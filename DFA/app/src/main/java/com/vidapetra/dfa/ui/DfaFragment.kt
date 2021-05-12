package com.vidapetra.dfa.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.myitsolver.baseandroidapp.fragments.BaseBindingFragment
import com.myitsolver.baseandroidapp.util.mutableLiveDataOf
import com.vidapetra.dfa.R
import com.vidapetra.dfa.databinding.FragmentDfaBinding
import com.vidapetra.dfa.logic.Router
import kotlinx.android.synthetic.main.fragment_dfa_list.*

class DfaFragment : BaseBindingFragment<FragmentDfaBinding>(){
    override val layoutResId: Int = R.layout.fragment_dfa

    val viewModel by viewModels<DfaViewModel>()

    override fun onBindingCreated(binding: FragmentDfaBinding) {
        super.onBindingCreated(binding)
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*recyclerView.adapter = DfaAdapter().apply {
            viewModel.items.observe(viewLifecycleOwner) {
                data = it
            }
        }*/
    }

}



class DfaViewModel : ViewModel(){
    val items = mutableLiveDataOf(listOf<Int>())

    init {
        items.postValue((1..20).toList())
    }

}

fun openDfa(){
    Router.loadFragment(DfaFragment())
}