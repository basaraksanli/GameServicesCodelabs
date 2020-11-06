package com.codelabs.gameservices.huawei.ui.main.gamebegin

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class GameBeginViewModel : ViewModel() {

    private val _navigateToQuizFragment = MutableLiveData<Boolean>()
    private val _navigateToAchievements = MutableLiveData<Boolean>()
    var accountName: MutableLiveData<String> = MutableLiveData()

    //Score object is static and it holds the highest score that the user did
    companion object {
        var score: MutableLiveData<Int>? = MutableLiveData(0)
    }

    fun setDisplayName(displayName: String?) {
        accountName.value = displayName
    }

    //Navigation Functions
    val navigateToQuizFragment: LiveData<Boolean>
        get() = _navigateToQuizFragment

    fun clickStartButton(view: View) {
        _navigateToQuizFragment.value = true
    }

    fun doneNavigatingQuiz() {
        _navigateToQuizFragment.value = false
    }

    val navigateToAchievements: LiveData<Boolean>
        get() = _navigateToAchievements

    fun showAchievements(view: View) {
        _navigateToAchievements.value = true
    }

    fun doneNavigationAchievements() {
        _navigateToAchievements.value = false
    }
}