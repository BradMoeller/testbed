package com.example.bradmoeller.myapplication.clean.data.model

sealed class Result<out T: Any> {
    class Success<out T: Any>(val data: T) : Result<T>()
    class Error(val responseCode: Int) : Result<Nothing>()
    class Exception(val exception: Throwable) : Result<Nothing>()
}