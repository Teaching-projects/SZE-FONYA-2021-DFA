package com.vidapetra.dfa.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.myitsolver.baseandroidapp.fragments.BaseBindingFragment
import com.myitsolver.baseandroidapp.util.mutableLiveDataOf
import com.vidapetra.dfa.R
import com.vidapetra.dfa.automata.Dfa
import com.vidapetra.dfa.automata.State
import com.vidapetra.dfa.automata.Transition
import com.vidapetra.dfa.databinding.FragmentDfaBinding
import com.vidapetra.dfa.logic.Router
import kotlinx.android.synthetic.main.fragment_dfa.*
import kotlinx.android.synthetic.main.fragment_dfa_list.*

class DfaFragment : BaseBindingFragment<FragmentDfaBinding>(){
    override val layoutResId: Int = R.layout.fragment_dfa

    private val viewModel by viewModels<DfaViewModel>()

    override fun onBindingCreated(binding: FragmentDfaBinding) {
        super.onBindingCreated(binding)
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnStart.setOnClickListener {
            if(checkInput(etInput.text.toString(), listOf('a', 'b'))){
                //
            } else {
                viewModel.showerror.postValue(true)
            }
        }
    }

}



class DfaViewModel : ViewModel(){

    var state1 = State(true, false)
    var state2 = State(false, true)
    val states = listOf<State>(state1, state2)

    var transition1 = Transition(state1, state2, 'a')
    var transition2 = Transition(state1, state1, 'b')
    var transition3 = Transition(state2, state2, 'b')
    var transition4 = Transition(state2, state1, 'a')
    val transitions = listOf<Transition>(transition1, transition2, transition3, transition4)

    var dfa = Dfa(states, transitions)

    var showerror = mutableLiveDataOf(false)

    init {

    }

}

fun openDfa(){
    Router.loadFragment(DfaFragment())
}

fun checkInput(input: String, abc: List<Char>) : Boolean{
    for (i in input){
        if(i !in abc){
            return false
        }
    }
    return true
}