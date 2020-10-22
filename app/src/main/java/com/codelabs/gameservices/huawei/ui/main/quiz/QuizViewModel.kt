package com.codelabs.gameservices.huawei.ui.main.quiz

import android.app.Activity
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codelabs.gameservices.huawei.GameServiceUtils
import com.codelabs.gameservices.huawei.constant.*
import com.codelabs.gameservices.huawei.model.QuestionModel
import com.codelabs.gameservices.huawei.ui.main.gamebegin.GameBeginViewModel


class QuizViewModel(activity: Activity) : ViewModel() {


    var questionObject: MutableLiveData<QuestionModel>? = null
    var score: MutableLiveData<Int>? = null
    var navigateBackToMenu: MutableLiveData<Boolean>? = null
    var activity : Activity?=null
    private var correctAnswerCount: Int
    private val gameServicesUtils : GameServiceUtils

    // TODO: Implement the ViewModel
    init {
        navigateBackToMenu = MutableLiveData(false)
        questionObject = MutableLiveData()
        score = MutableLiveData(0)

        this.activity = activity
        gameServicesUtils= GameServiceUtils(activity, null)

        correctAnswerCount=0

        gameServicesUtils.resetSteps(SOPHISTICATEDACHIEVEMENTID, true)
        gameServicesUtils.resetSteps(PRIMITIVEACHIEVEMENTID, true)
        setQuestion()
    }

    private var count = 0

    private fun setQuestion() {
        count++
        if (count > 15) {
            if (GameBeginViewModel.score!!.value!! < score!!.value!!)
                GameBeginViewModel.score!!.value = score!!.value!!
            navigateBackToMenu!!.value = true
        } else {
            questionObject!!.value = QUESTIONS.random()
            while (questionObject!!.value!!.isAsked) {
                questionObject!!.value = QUESTIONS.random()
            }
            questionObject!!.value!!.isAsked = true
        }
    }

    fun navigateBackDone() {
        for (question in QUESTIONS)
            question.isAsked = false
        navigateBackToMenu!!.value = false
    }


    fun chooseA(view: View) {
        if (questionObject!!.value!!.answer == 0)
            correctAnswer()
        else
            wrongAnswer()
        setQuestion()
    }

    fun chooseB(view: View) {
        if (questionObject!!.value!!.answer == 1)
            correctAnswer()
        else
            wrongAnswer()
        setQuestion()
    }

    fun chooseC(view: View) {
        if (questionObject!!.value!!.answer == 2)
            correctAnswer()
        else
            wrongAnswer()
        setQuestion()
    }

    fun chooseD(view: View) {
        if (questionObject!!.value!!.answer == 3)
            correctAnswer()
        else
           wrongAnswer()
        setQuestion()
    }
    private fun correctAnswer(){
        score!!.value = score!!.value?.plus(10)
        correctAnswerCount++

        if(correctAnswerCount==10)
            gameServicesUtils.revealAchievement(SOPHISTICATEDACHIEVEMENTID)

        if(questionObject!!.value!!.questionID== 9)
            gameServicesUtils.unlockAchievement(SCIENTISTACHIEVEMENTID)

        gameServicesUtils.unlockAchievement(NEWBIEACHIEVEMENTID)
        gameServicesUtils.increment(LEARNINGACHIEVEMENTID,true)
        gameServicesUtils.increment(SOPHISTICATEDACHIEVEMENTID, true)
    }
    private fun wrongAnswer(){
        score!!.value = score!!.value?.minus(5)
        gameServicesUtils.increment(PRIMITIVEACHIEVEMENTID, true)
    }


}