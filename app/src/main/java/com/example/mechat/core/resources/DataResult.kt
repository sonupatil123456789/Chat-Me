package com.example.mechat.core.resources


sealed class DataResult<T>(val data: T? = null, val message: String? = null, val error: AppException? = null) {
    class Success<T>(message: String, data: T) : DataResult<T>(data = data,message = message)
    class Error<T>(error: AppException, data: T? = null) : DataResult<T>(data = data, error = error)
}


