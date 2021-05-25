package com.vidapetra.dfa.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.myitsolver.baseandroidapp.fragments.BaseBindingFragment
import com.myitsolver.baseandroidapp.util.getItemOrNull
import com.myitsolver.baseandroidapp.util.mutableLiveDataOf
import com.myitsolver.baseandroidapp.util.recyclerview.BaseDiffRecyclerviewAdapter
import com.myitsolver.baseandroidapp.util.recyclerview.addVerticalLayoutManager
import com.vidapetra.dfa.R
import com.vidapetra.dfa.automata.Dfa
import com.vidapetra.dfa.automata.getDfaListFromAssets
import com.vidapetra.dfa.databinding.FragmentDfaListBinding
import com.vidapetra.dfa.logic.Router
import com.vidapetra.dfa.logic.applicationContext
import kotlinx.android.synthetic.main.dfa_item.view.*
import kotlinx.android.synthetic.main.fragment_dfa_list.*
import kotlinx.android.synthetic.main.fragment_simulation.*

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
                openDfa(it)
            }
        }
    }

}



class DfaListViewModel : ViewModel(){
    val items = mutableLiveDataOf(listOf<Dfa>())
    val dfas = getDfaListFromAssets(applicationContext)

    init {
        items.postValue(dfas)
    }

}

fun openDfaList(){
    Router.loadFragment(DfaListFragment())
}

class DfaAdapter : BaseDiffRecyclerviewAdapter<Dfa>(){
    override fun getViewHolderLayoutId(viewType: Int) = R.layout.dfa_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int, viewType: Int) {
        val name = data?.getItemOrNull(position)?.imageName
        val resourceId = applicationContext.resources.getIdentifier(name, "drawable", applicationContext.packageName)
        holder.itemView.dfaImage.setImageResource(resourceId)
        holder.itemView.tvAbc.text = data?.getItemOrNull(position)?.abc?.let { getAbcString(it) }
        holder.itemView.tvDesc.text = data?.getItemOrNull(position)?.description
    }


}

fun getAbcString(chars: List<Char>): String{
    var abc = "Σ = {"
    for(c in chars) abc += "$c, "
    abc = abc.substring(0, abc.length - 2)
    abc += "}"
    return abc
}