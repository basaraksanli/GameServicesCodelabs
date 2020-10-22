package com.codelabs.gameservices.huawei.ui.main.gamebegin

import android.app.Activity
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codelabs.gameservices.huawei.GameServiceUtils
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.jos.JosApps


class GameBeginViewModel(activity: Activity) : ViewModel() {

    private val _navigateToQuizFragment = MutableLiveData<Boolean>()
    var accountName: MutableLiveData<String> = MutableLiveData()
    var activity: Activity? = null
    var gameServiceUtils: GameServiceUtils? = null


    companion object {
        var score: MutableLiveData<Int>? = MutableLiveData(0)
    }

    init {
        this.activity = activity

        JosApps.getJosAppsClient(activity).init()

        gameServiceUtils = GameServiceUtils(activity, this)
        gameServiceUtils!!.signIn()


    }

    val navigateToQuizFragment: LiveData<Boolean>
        get() = _navigateToQuizFragment

    fun clickStartButton(view: View) {
        _navigateToQuizFragment.value = true
    }

    fun doneNavigating() {
        _navigateToQuizFragment.value = false
    }

    fun showAchievements(view: View) {
        gameServiceUtils!!.showAchivementList()
    }




}