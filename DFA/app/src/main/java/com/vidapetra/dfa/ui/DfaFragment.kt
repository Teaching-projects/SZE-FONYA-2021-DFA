package com.vidapetra.dfa.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.myitsolver.baseandroidapp.fragments.BaseBindingFragment
import com.myitsolver.baseandroidapp.util.ifContainsInt
import com.myitsolver.baseandroidapp.util.mutableLiveDataOf
import com.vidapetra.dfa.R
import com.vidapetra.dfa.automata.Dfa
import com.vidapetra.dfa.automata.getDfaListFromAssets
import com.vidapetra.dfa.databinding.FragmentDfaBinding
import com.vidapetra.dfa.logic.Router
import com.vidapetra.dfa.logic.applicationContext
import kotlinx.android.synthetic.main.fragment_dfa.*

class DfaFragment : BaseBindingFragment<FragmentDfaBinding>(){
    override val layoutResId: Int = R.layout.fragment_dfa

    private val viewModel by viewModels<DfaViewModel>()

    override fun onBindingCreated(binding: FragmentDfaBinding) {
        super.onBindingCreated(binding)
        binding.vm = viewModel
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.ifContainsInt("dfa") {
            viewModel.dfaId = it
            for(d in viewModel.dfas) {
                if(d.id == viewModel.dfaId) viewModel.dfa = d
            }
        }
        btnStart.setOnClickListener {
            if(checkInput(etInput.text.toString(), listOf('a', 'b'))){
                viewModel.showerror.postValue(false)
                openSimulation(viewModel.dfaId, etInput.text.toString())
            } else {
                viewModel.showerror.postValue(true)
            }
        }
        val name = viewModel.dfa.imageName
        val resourceId = applicationContext.resources.getIdentifier(name, "drawable", applicationContext.packageName)
        dfa.setImageResource(resourceId)

        tvDesc.text = "Adj meg egy bemenetet a ${getAbcString(viewModel.dfa.abc)} ábécé betűit felhasználva!"
        tvTitle.text = viewModel.dfa.description
    }

}



open class DfaViewModel : ViewModel(){

    var showerror = mutableLiveDataOf(false)
    var dfaId: Int = 0
    val dfas = getDfaListFromAssets(applicationContext)
    lateinit var dfa: Dfa

    init {

    }

}

fun openDfa(dfa: Dfa){
    Router.loadFragment(DfaFragment().apply {
        arguments = bundleOf("dfa" to dfa.id)
    }, params = {forceNoAddAgain()})
}

fun checkInput(input: String, abc: List<Char>) : Boolean{
    for (i in input){
        if(i !in abc){
            return false
        }
    }
    return true
}