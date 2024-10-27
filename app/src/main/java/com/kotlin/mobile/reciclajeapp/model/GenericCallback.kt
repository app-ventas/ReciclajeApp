package com.kotlin.mobile.reciclajeapp.model

interface GenericCallback<T> {
    fun onSuccess(result: T)
    fun onError(error: String)
}