package com.codelabs.gameservices.huawei.ui.main.achievements

import AchievementListAdapter
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.jos.games.AchievementsClient
import com.huawei.hms.jos.games.achievement.Achievement


class AchievementsViewModel : ViewModel() {

    private val _achievementList : MutableLiveData<MutableList<Achievement>> = MutableLiveData()
    val achievementList: LiveData<MutableList<Achievement>> get() = _achievementList

    var progressBarVisibility = MutableLiveData<Int>(View.VISIBLE)

    //This is the variable to decide navigate Menu Page. Fragment is observing this.
    private val _navigateToMenu = MutableLiveData<Boolean>()

    //This is the function for getting achievements from AGC. Since it is an async task, It does not return anything. However we are adding achievements to a list as the Achievements Items are downloaded
    fun getAchievements(achievementClient: AchievementsClient){
        // TODO: get achievements from the client and add each achievement to the custom adapter list.
        val task: Task<List<Achievement>> =
            achievementClient.getAchievementList(true)
        task.addOnSuccessListener(OnSuccessListener { data ->
            if (data == null) {
                Log.w("Achievement", "achievement list is null")
                return@OnSuccessListener
            }
            val list = mutableListOf<Achievement>()
            for (achievement in data) {
                list.add(achievement)
            }

            _achievementList.value = list

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