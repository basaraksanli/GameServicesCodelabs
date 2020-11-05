package com.codelabs.gameservices.huawei.ui.main

interface ResultListener {
    fun onSuccess(result: String)
    fun onFailure(errorMessage: String? = null)
}