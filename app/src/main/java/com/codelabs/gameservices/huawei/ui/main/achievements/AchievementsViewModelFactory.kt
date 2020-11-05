
package com.codelabs.gameservices.huawei.ui.main.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//This is the View Model Factory for getting fragment object from the fragment to the View Model Object
class AchievementsViewModelFactory : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AchievementsViewModel::class.java)) {
            return AchievementsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}