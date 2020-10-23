
package com.codelabs.gameservices.huawei.ui.main.quiz

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//ViewModelFactory to get activity to ViewModel from the activity
class QuizViewModelFactory (
    private val activity: Activity) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}