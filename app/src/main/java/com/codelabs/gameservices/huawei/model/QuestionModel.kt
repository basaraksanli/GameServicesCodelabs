package com.codelabs.gameservices.huawei.model

/*
*question model is simple. containing question, possible answers and correct answer as well as its id and the boolean is asked before
*/

data class QuestionModel(
    val questionID:Int,
    val question : String,
    val ansA: String,
    val ansB:String,
    val ansC:String,
    val ansD:String,
    val answer: Int,
    var isAsked: Boolean
)