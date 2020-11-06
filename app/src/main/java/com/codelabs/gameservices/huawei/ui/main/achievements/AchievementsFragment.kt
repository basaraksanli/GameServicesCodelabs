package com.codelabs.gameservices.huawei.ui.main.achievements

import AchievementListAdapter
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
import com.codelabs.gameservices.huawei.GameServiceManager
import com.codelabs.gameservices.huawei.R
import com.codelabs.gameservices.huawei.databinding.AchievementsFragmentBinding

class AchievementsFragment : Fragment() {

    //This is observed by the fragment in order to set it to the recycle View
    private var achievementListAdapter: AchievementListAdapter? = null

    private val mGameServiceManager by lazy { GameServiceManager(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //As we are using MVVM Pattern we need to set our binding object as Fragment's Binding Object
        val binding: AchievementsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.achievements_fragment, container, false)

        //Because we are using fragment object in the ViewModel File, we need to create a factory object
        val factory = AchievementsViewModelFactory()
        //After we set our factory object, we initiate our ViewModel Object
        val achievementsViewModel = ViewModelProvider(this, factory ).get(AchievementsViewModel::class.java)

        //We are setting the parameters of the binding properties
        binding.viewmodel = achievementsViewModel
        binding.lifecycleOwner = this

        //Init of recycleView
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.achievementListView)

        //Setting Layout manager for recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        achievementListAdapter = AchievementListAdapter(requireContext())
        recyclerView.adapter = achievementListAdapter

        mGameServiceManager.getAchievementClient()?.let { achievementsViewModel.getAchievements(it) }

        //Observe user interaction for navigation to the other pages
        achievementsViewModel.navigateToMenu.observe(
            viewLifecycleOwner,
            Observer {
                if (it == true) {
                    this.findNavController().navigate(R.id.goBackToMainMenuNav)
                    achievementsViewModel.doneNavigating()
                }
            })

        achievementsViewModel.achievementList.observe(
            viewLifecycleOwner,
            Observer {
                achievementListAdapter?.setAchievementList(it)
            }
        )

        return binding.root
    }

}