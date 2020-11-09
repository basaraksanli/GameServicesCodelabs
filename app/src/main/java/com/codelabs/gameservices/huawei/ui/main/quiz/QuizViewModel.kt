package com.codelabs.gameservices.huawei.ui.main.quiz

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codelabs.gameservices.huawei.constant.ANSWER_POINT_CORRECT
import com.codelabs.gameservices.huawei.constant.ANSWER_POINT_WRONG
import com.codelabs.gameservices.huawei.constant.QUESTIONS
import com.codelabs.gameservices.huawei.model.QuestionModel
import com.codelabs.gameservices.huawei.ui.main.gamebegin.GameBeginViewModel


class QuizViewModel : ViewModel() {

    var questionObject: MutableLiveData<QuestionModel>? = null
    var navigateBackToMenu: MutableLiveData<Boolean>? = null

    val score: MutableLiveData<Int> = MutableLiveData(0)

    private val _questionId: MutableLiveData<Int> = MutableLiveData()
    val questionId: LiveData<Int> get() = _questionId

    private val _correctAnswerCount: MutableLiveData<Int> = MutableLiveData()
    val correctAnswerCount: LiveData<Int> get() = _correctAnswerCount

    private val _wrongAnswerCount: MutableLiveData<Int> = MutableLiveData()
    val wrongAnswerCount: LiveData<Int> get() = _wrongAnswerCount

    init {
        navigateBackToMenu = MutableLiveData(false)
        questionObject = MutableLiveData()
        //initialize current game score
        score.value = 0

        _correctAnswerCount.value = 0
        _wrongAnswerCount.value = 0

        //Start Displaying Questions
        setQuestion()
    }

    private var count = 0

    private fun setQuestion() {
        //For each question, increment count and when it reaches 15 finish the game
        count++
        if (count > 15) {
            //Set highest score if score is higher then before
            if (GameBeginViewModel.score!!.value!! < score.value!!)
                GameBeginViewModel.score!!.value = score.value!!
            navigateBackToMenu!!.value = true
        } else {
            //Display random question. If random question is already asked, get another random question loop until it finds a new one
            questionObject!!.value = QUESTIONS.random()
            while (questionObject!!.value!!.isAsked) {
                questionObject!!.value = QUESTIONS.random()
            }
            //mark the question as asked
            questionObject!!.value!!.isAsked = true

            _questionId.value = questionObject!!.value!!.questionID
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
        score.value = score.value?.plus(ANSWER_POINT_CORRECT)
        _correctAnswerCount.value = _correctAnswerCount.value?.plus(1)
    }

    private fun wrongAnswer(){
        //if user selects the wrong answer decrease score by specified point
        if (score.value!! >= 5){
            score.value = score.value?.minus(ANSWER_POINT_WRONG)
        }
        _wrongAnswerCount.value = _wrongAnswerCount.value?.plus(1)
    }

}