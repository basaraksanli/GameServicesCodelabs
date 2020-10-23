package com.codelabs.gameservices.huawei.ui.main.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codelabs.gameservices.huawei.R
import com.codelabs.gameservices.huawei.databinding.QuizFragmentBinding
import com.codelabs.gameservices.huawei.Utils
import com.codelabs.gameservices.huawei.ui.main.gamebegin.GameBeginViewModel

class QuizFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: QuizFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.quiz_fragment, container, false)

        // Get a reference to the ViewModel associated with this fragment.
        val factory = QuizViewModelFactory(this.requireActivity())
        val quizViewModel =ViewModelProvider(this, factory).get(QuizViewModel::class.java)

        //We are setting the parameters of the binding properties
        binding.viewmodel = quizViewModel
        binding.lifecycleOwner = this

        //Navigation Function
        quizViewModel.navigateBackToMenu!!.observe(viewLifecycleOwner, Observer {
            if(it ==true) {
                Utils.buildAlertDialogAndNavigate(this)
                quizViewModel.navigateBackDone()
            }
        })

        return binding.root
    }

}