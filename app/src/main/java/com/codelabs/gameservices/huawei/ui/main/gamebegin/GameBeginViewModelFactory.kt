
package com.codelabs.gameservices.huawei.ui.main.gamebegin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//ViewModelFactory to get activity from the Activity into ViewModel
class GameBeginViewModelFactory : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameBeginViewModel::class.java)) {
            return GameBeginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}