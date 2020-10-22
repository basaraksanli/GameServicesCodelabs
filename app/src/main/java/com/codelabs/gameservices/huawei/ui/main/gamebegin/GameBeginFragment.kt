package com.codelabs.gameservices.huawei.ui.main.gamebegin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codelabs.gameservices.huawei.R
import com.codelabs.gameservices.huawei.databinding.FragmentGameBeginBinding


class GameBeginFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentGameBeginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_begin, container, false)

        // Get a reference to the ViewModel associated with this fragment.

        val factory = GameBeginViewModelFactory(this.requireActivity())
        val gameBeginViewModel =ViewModelProvider(this, factory).get(GameBeginViewModel::class.java)
        


        binding.viewmodel = gameBeginViewModel
        binding.lifecycleOwner = this


        gameBeginViewModel.navigateToQuizFragment.observe(
            viewLifecycleOwner,
            Observer {
                if (it == true) {
                    this.findNavController().navigate(R.id.start_game_navigation)
                    gameBeginViewModel.doneNavigating()
                }
            })

        return binding.root


    }


}