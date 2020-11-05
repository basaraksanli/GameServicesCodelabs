package com.codelabs.gameservices.huawei.ui.main.gamebegin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codelabs.gameservices.huawei.GameServiceManager
import com.codelabs.gameservices.huawei.R
import com.codelabs.gameservices.huawei.databinding.FragmentGameBeginBinding
import com.codelabs.gameservices.huawei.ui.main.ResultListener


class GameBeginFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val mGameServiceManager by lazy { GameServiceManager(requireActivity()) }
    private lateinit var gameBeginViewModel: GameBeginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentGameBeginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_begin, container, false)

        // Get a reference to the ViewModel associated with this fragment.
        val factory = GameBeginViewModelFactory()
        gameBeginViewModel =
            ViewModelProvider(this, factory).get(GameBeginViewModel::class.java)


        //We are setting the parameters of the binding properties
        binding.viewmodel = gameBeginViewModel
        binding.lifecycleOwner = this

        //Navigation Observation
        gameBeginViewModel.navigateToQuizFragment.observe(
            viewLifecycleOwner,
            Observer {
                if (it == true) {
                    this.findNavController().navigate(R.id.start_game_navigation)
                    gameBeginViewModel.doneNavigatingQuiz()
                }
            })
        //Navigation Observation
        gameBeginViewModel.navigateToAchievements.observe(
            viewLifecycleOwner,
            Observer {
                if (it == true) {
                    this.findNavController().navigate(R.id.showAchievementListNav)
                    gameBeginViewModel.doneNavigationAchievements()
                }
            })

        return binding.root


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mGameServiceManager.signIn(object : ResultListener {
            override fun onSuccess(result: String) {
                gameBeginViewModel.setDisplayName(result)
            }

            override fun onFailure(errorMessage: String?) {

            }

        })
    }


}