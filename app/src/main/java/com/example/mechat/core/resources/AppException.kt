package com.example.mechat.core.resources

import com.example.smartjobreminder.core.resources.ErrorCode


class AppException(
    override val message: String,
    val title: String,
    val error: Exception? = null,
    val errorCode: ErrorCode,
    val stack: Array<StackTraceElement>
) : Exception(message, error) {

    override fun toString(): String {
        return "AppException: (message: $message, " +
                "title: $title, " +
                "errorCode: $errorCode, " +
                "exceptionType: ${error?.javaClass?.name}, " +
                "exceptionDetail: ${error?.toString()}, " +
                "stackTrace: ${stack.toString()})"
    }
}