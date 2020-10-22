
package com.codelabs.gameservices.huawei.ui.main.achievements

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AchievementsViewModelFactory (
    private val fragment: Fragment) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AchievementsViewModel::class.java)) {
            return AchievementsViewModel(fragment) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}