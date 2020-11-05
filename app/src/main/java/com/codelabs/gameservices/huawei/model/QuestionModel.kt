package com.codelabs.gameservices.huawei.model

//question model is simple. containing question, possible answers and correct answer as well as its id and the boolean is asked before

data class QuestionModel(
    val questionID:Int,
    val question : String,
    val a1: String,
    val a2:String,
    val a3:String,
    val a4:String,
    val answer: Int,
    var isAsked: Boolean
)