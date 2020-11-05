package com.codelabs.gameservices.huawei.ui.main.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codelabs.gameservices.huawei.GameServiceManager
import com.codelabs.gameservices.huawei.R
import com.codelabs.gameservices.huawei.databinding.QuizFragmentBinding
import com.codelabs.gameservices.huawei.Utils
import com.codelabs.gameservices.huawei.constant.*
import com.codelabs.gameservices.huawei.ui.main.gamebegin.GameBeginViewModel

class QuizFragment : Fragment() {

    private val mGameServiceManager by lazy { GameServiceManager(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: QuizFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.quiz_fragment, container, false)

        // Get a reference to the ViewModel associated with this fragment.
        val factory = QuizViewModelFactory()
        val quizViewModel =ViewModelProvider(this, factory).get(QuizViewModel::class.java)

        //We are setting the parameters of the binding properties
        binding.viewmodel = quizViewModel
        binding.lifecycleOwner = this

        //Navigation Function
        quizViewModel.navigateBackToMenu!!.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                Utils.buildAlertDialogAndNavigate(this)
                quizViewModel.navigateBackDone()
            }
        })

        quizViewModel.correctAnswerCount.observe(
            viewLifecycleOwner,
            Observer {

                //for the first correct answer unlock achievement newbie
                if (it == 1) {
                    mGameServiceManager.unlockAchievement(ACHIEVEMENT_ID_NEWBIE)
                }

                //if correct answerCount reaches 10 in one try, It will reveal the achievement
                if (it == 10) {
                    mGameServiceManager.revealAchievement(ACHIEVEMENT_ID_SOPHISTICATED)
                }

                //for every correct answer increment the achievements steps of the achievements below
                mGameServiceManager.increment(ACHIEVEMENT_ID_LEARNING,true)
                mGameServiceManager.increment(ACHIEVEMENT_ID_SOPHISTICATED, true)
            }
        )

        quizViewModel.questionId.observe(
            viewLifecycleOwner,
            Observer {
                //if the question id 9 is answered, unlock the achievement Scientist
                if(it == 9)
                    mGameServiceManager.unlockAchievement(ACHIEVEMENT_ID_SCIENTIST)
            }
        )

        quizViewModel.wrongAnswerCount.observe(
            viewLifecycleOwner,
            Observer {
                mGameServiceManager.increment(ACHIEVEMENT_ID_PRIMITIVE, true)
            }
        )

        return binding.root
    }

}