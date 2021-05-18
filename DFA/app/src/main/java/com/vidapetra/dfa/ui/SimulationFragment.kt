package com.vidapetra.dfa.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.myitsolver.baseandroidapp.fragments.BaseBindingFragment
import com.myitsolver.baseandroidapp.util.ifContainsInt
import com.myitsolver.baseandroidapp.util.ifContainsString
import com.myitsolver.baseandroidapp.util.mutableLiveDataOf
import com.vidapetra.dfa.R
import com.vidapetra.dfa.automata.Dfa
import com.vidapetra.dfa.automata.State
import com.vidapetra.dfa.automata.Transition
import com.vidapetra.dfa.databinding.FragmentSimulationBinding
import com.vidapetra.dfa.logic.Router
import com.vidapetra.dfa.logic.applicationContext
import kotlinx.android.synthetic.main.fragment_simulation.*


class SimulationFragment : BaseBindingFragment<FragmentSimulationBinding>(){
    override val layoutResId: Int = com.vidapetra.dfa.R.layout.fragment_simulation

    private val viewModel by viewModels<SimulationViewModel>()

    override fun onBindingCreated(binding: FragmentSimulationBinding) {
        super.onBindingCreated(binding)
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*arguments?.ifContainsInt("dfa") {
            viewModel.dfa = it
        }*/
        arguments?.ifContainsString("input") {
            viewModel.end.postValue(it)
        }

        // ide még kell ha más automata jön
        dfa.setImageResource(R.drawable.dfa1_1)


        next.setOnClickListener {
            var aktbegin = viewModel.begin.value ?: ""
            var aktend = viewModel.end.value ?: ""
            if(aktend.isNotEmpty()) {
                aktbegin = aktbegin.plus(aktend[0])
                aktend = aktend.removeRange(startIndex = 0, endIndex = 1)
                viewModel.begin.postValue(aktbegin)
                viewModel.end.postValue(aktend)
                viewModel.dfa.aktState = viewModel.dfa.nextState(aktbegin[aktbegin.length - 1])
                if(aktend.isNotEmpty()) {
                    val name = "dfa1_" + (viewModel.dfa.aktState + 1)
                    val resourceId = resources.getIdentifier(name, "drawable", context?.getPackageName())
                    dfa.setImageResource(resourceId)
                } else {
                    // tovább nyíl csere


                    val name = "dfa1_" + (viewModel.dfa.aktState + 1) + "_end"
                    val resourceId = resources.getIdentifier(name, "drawable", context?.getPackageName())
                    dfa.setImageResource(resourceId)
                }
            }
        }
        back.setOnClickListener {
            var aktbegin = viewModel.begin.value ?: ""
            var aktend = viewModel.end.value ?: ""
            if(aktbegin.isNotEmpty()) {
                viewModel.dfa.aktState = viewModel.dfa.previousState(aktbegin[aktbegin.length - 1])
                aktend = aktbegin[aktbegin.length - 1] + aktend
                aktbegin = aktbegin.substring(0, aktbegin.length - 1)
                viewModel.begin.postValue(aktbegin)
                viewModel.end.postValue(aktend)
                val name = "dfa1_" + (viewModel.dfa.aktState + 1)
                val resourceId = resources.getIdentifier(name, "drawable", context?.getPackageName())
                dfa.setImageResource(resourceId)

            }
        }
    }

}



class SimulationViewModel : ViewModel(){

    var state1 = State(true, false)
    var state2 = State(false, true)
    val states = listOf(state1, state2)

    var transition1 = Transition(0, 1, 'a')
    var transition2 = Transition(0, 0, 'b')
    var transition3 = Transition(1, 1, 'b')
    var transition4 = Transition(1, 0, 'a')
    val transitions = listOf<Transition>(transition1, transition2, transition3, transition4)

    var dfa = Dfa(states, transitions)

    var begin = mutableLiveDataOf("")
    var end = mutableLiveDataOf("")

    init {

    }

}

fun openSimulation(dfa: Int, input: String){
    Router.loadFragment(SimulationFragment().apply {
        arguments = bundleOf("input" to input)
    }, params = {forceNoAddAgain()})
}
