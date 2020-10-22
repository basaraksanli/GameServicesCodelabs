package com.codelabs.gameservices.huawei.ui.main.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.gameservices.huawei.R
import com.codelabs.gameservices.huawei.databinding.AchievementsFragmentBinding

class AchievementsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: AchievementsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.achievements_fragment, container, false)

        val factory = AchievementsViewModelFactory(this)
        val achievementsModelView = ViewModelProvider(this, factory ).get(AchievementsViewModel::class.java)

        binding.viewmodel = achievementsModelView
        binding.lifecycleOwner = this

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.achievementListView)

        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        achievementsModelView.achievementListAdapter!!.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = it
        })

        achievementsModelView.navigateToMenu.observe(
            viewLifecycleOwner,
            Observer {
                if (it == true) {
                    this.findNavController().navigate(R.id.goBackToMainMenuNav)
                    achievementsModelView.doneNavigating()
                }
            })


        return binding.root
    }

}