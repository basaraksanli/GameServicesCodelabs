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

    private var correctAnswerCount: Int
    private val gameServicesUtils : GameServiceUtils


    init {
        navigateBackToMenu = MutableLiveData(false)
        questionObject = MutableLiveData()
        //initialize current game score
        score = MutableLiveData(0)

        //initialize GameService
        gameServicesUtils= GameServiceUtils(activity, null)

        correctAnswerCount=0

        //Start Displaying Questions
        setQuestion()
    }

    private var count = 0


    private fun setQuestion() {
        //For each question, increment count and when it reaches 15 finish the game
        count++
        if (count > 15) {
            //Set highest score if score is higher then before
            if (GameBeginViewModel.score!!.value!! < score!!.value!!)
                GameBeginViewModel.score!!.value = score!!.value!!
            navigateBackToMenu!!.value = true
        } else {
            //Display random question. If random question is already asked, get another random question loop until it finds a new one
            questionObject!!.value = QUESTIONS.random()
            while (questionObject!!.value!!.isAsked) {
                questionObject!!.value = QUESTIONS.random()
            }
            //mark the question as asked
            questionObject!!.value!!.isAsked = true
        }
    }
    //navigation function
    fun navigateBackDone() {
        for (question in QUESTIONS)
            question.isAsked = false
        navigateBackToMenu!!.value = false
    }

    //For the choices assert if the answer is correct and call correctAnswer or wrongAnswer function
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
        //increment score
        score!!.value = score!!.value?.plus(10)
        correctAnswerCount++

        //if correct answerCount reaches 10 in one try, It will reveal the achievement
        if(correctAnswerCount==10)
            gameServicesUtils.revealAchievement(SOPHISTICATEDACHIEVEMENTID)

        //if the question id 9 is answered, unlock the achievement Scientist
        if(questionObject!!.value!!.questionID== 9)
            gameServicesUtils.unlockAchievement(SCIENTISTACHIEVEMENTID)

        //for the first correct answer unlock achievement newbie
        gameServicesUtils.unlockAchievement(NEWBIEACHIEVEMENTID)

        //for every correct answer increment the achievements steps of the achievements below
        gameServicesUtils.increment(LEARNINGACHIEVEMENTID,true)
        gameServicesUtils.increment(SOPHISTICATEDACHIEVEMENTID, true)
    }
    private fun wrongAnswer(){
        //if user selects the wrong answer increment primitive achievement and decrease score by 5
        score!!.value = score!!.value?.minus(5)
        gameServicesUtils.increment(PRIMITIVEACHIEVEMENTID, true)
    }

}