package com.example.learningretrofit.data

sealed class Result<T> (
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(data: T?, message: String?) : Result<T>(data, message)
}
/**
 * To know what is happening in regard to the API
 * If the API works or if the API doesn't work
 */