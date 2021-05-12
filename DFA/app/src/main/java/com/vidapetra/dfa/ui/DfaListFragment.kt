package com.vidapetra.dfa.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.myitsolver.baseandroidapp.fragments.BaseBindingFragment
import com.myitsolver.baseandroidapp.util.mutableLiveDataOf
import com.myitsolver.baseandroidapp.util.recyclerview.BaseDiffRecyclerviewAdapter
import com.myitsolver.baseandroidapp.util.recyclerview.addVerticalLayoutManager
import com.vidapetra.dfa.R
import com.vidapetra.dfa.databinding.FragmentDfaListBinding
import com.vidapetra.dfa.logic.Router
import kotlinx.android.synthetic.main.fragment_dfa_list.*

class DfaListFragment : BaseBindingFragment<FragmentDfaListBinding>(){
    override val layoutResId: Int = R.layout.fragment_dfa_list

    val viewModel by viewModels<DfaListViewModel>()

    override fun onBindingCreated(binding: FragmentDfaListBinding) {
        super.onBindingCreated(binding)
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = DfaAdapter().apply {
            viewModel.items.observe(viewLifecycleOwner) {
                data = it
            }
            listener {
                openDfa()
            }
        }
    }

}



class DfaListViewModel : ViewModel(){
    val items = mutableLiveDataOf(listOf<Int>())

    init {
        items.postValue((1..20).toList())
    }

}

fun openDfaList(){
    Router.loadFragment(DfaListFragment())
}

class DfaAdapter : BaseDiffRecyclerviewAdapter<Int>(){
    override fun getViewHolderLayoutId(viewType: Int) = R.layout.dfa_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int, viewType: Int) {

    }


}