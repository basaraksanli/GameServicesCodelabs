package com.codelabs.gameservices.huawei.ui.main.achievements

import AchievementListAdapter
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codelabs.gameservices.huawei.GameServiceUtils
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.jos.games.AchievementsClient
import com.huawei.hms.jos.games.achievement.Achievement


class AchievementsViewModel(private val fragment: Fragment) : ViewModel() {
    private var achievementList : MutableList<Achievement> = mutableListOf()
    private val gameServiceUtils : GameServiceUtils = GameServiceUtils(fragment.requireActivity(), null)
    private val achievementClient : AchievementsClient

    //This is observed by the fragment in order to set it to the recycle View
    var achievementListAdapter: MutableLiveData<AchievementListAdapter>? = MutableLiveData()

    var progressBarVisibility = MutableLiveData<Int>(View.VISIBLE)

    //This is the variable to decide navigate Menu Page. Fragment is observing this.
    private val _navigateToMenu = MutableLiveData<Boolean>()

    init {
        //Get Achievements Client from the gameServiceUtils
        achievementClient = gameServiceUtils.achievementClient!!
        getAchievements()
    }
    //This is the function for getting achievements from AGC. Since it is an async task, It does not return anything. However we are adding achievements to a list as the Achievements Items are downloaded
    private fun getAchievements(){
        val task: Task<List<Achievement>> =
            achievementClient.getAchievementList(true)
        task.addOnSuccessListener(OnSuccessListener { data ->
            if (data == null) {
                Log.w("Achievement", "achievement list is null")
                return@OnSuccessListener
            }
            for (achievement in data) {
                achievementList.add(achievement)
            }
            achievementListAdapter!!.value = AchievementListAdapter(achievementList, fragment.requireContext())
            progressBarVisibility.value = View.GONE
        }).addOnFailureListener { e ->
            if (e is ApiException) {
                val result = "rtnCode:" +
                        (e as ApiException).statusCode
                Log.e("Achievement", result)
            }
        }
    }
    //We don't share our _navigateToMenu variable with the Fragment. It is private. We only return this variable as navigateToMenu and we don't give privilege the fragment to change it
    val navigateToMenu: LiveData<Boolean>
        get() = _navigateToMenu

    //navigation functions
    fun clickBackButton(view: View) {
        _navigateToMenu.value = true
    }

    fun doneNavigating() {
        _navigateToMenu.value = false
    }

}