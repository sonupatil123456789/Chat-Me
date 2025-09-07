import android.util.Log
import com.example.mechat.core.resources.AppException
import com.example.smartjobreminder.core.resources.ErrorCode
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class ExceptionHandler {
    companion object {
        fun handleDatabaseException(error: Exception, stackTrace: Array<StackTraceElement>): AppException {
            return AppException(
                title = ErrorCode.DATABASE_ERROR.message,
                message = "An unexpected error occurred with the local database.",
                errorCode = ErrorCode.DATABASE_ERROR,
                error = error,
                stack = stackTrace
            )
        }

        fun handleFirebaseDbException(error: Exception, stackTrace: Array<StackTraceElement>): AppException {
            return when (error) {
                is FirebaseNetworkException -> {
                    return AppException(
                        title = ErrorCode.NO_INTERNET_CONNECTION.title,
                        message = "The connection timed out. Please check your internet and try again.",
                        errorCode = ErrorCode.NO_INTERNET_CONNECTION,
                        error = error,
                        stack = stackTrace
                    )
                }
                else ->{
                    AppException(
                        title = ErrorCode.FIREBASE_ERROR.title,
                        message = error.toString(),
                        errorCode = ErrorCode.FIREBASE_ERROR,
                        error = error,
                        stack = stackTrace
                    )
                }

            }
        }
//        fun handleNetworkException(error: Exception, stackTrace: StackTraceElement?): AppException {
//            return when (error) {
//                is retrofit2.HttpException -> {
//                    val statusCode = error.code()
//
//                    // Assuming response data handling similar to Dio
//                    // Note: This would need to be adjusted based on your actual Retrofit response structure
//                    val responseMessage = try {
//                        error.response()?.errorBody()?.string()?.let {
//                            // This is a simplified placeholder - you'd need a proper JSON parser here
//                            if (it.contains("message")) {
//                                it.substringAfter("message").substringAfter(":").substringBefore(",").trim()
//                            } else null
//                        }
//                    } catch (e: Exception) {
//                        null
//                    }
//
//                    AppException(
//                        message = getDisplayMessageForStatusCode(statusCode, responseMessage),
//                        errorCode = getErrorCodeForStatusCode(statusCode),
//                        error = error,
//                        stack = stackTrace
//                    )
//                }
//                is SocketException, is IOException -> {
//                    AppException(
//                        message = "The connection timed out. Please check your internet and try again.",
//                        errorCode = ErrorCode.NO_INTERNET_CONNECTION,
//                        error = error,
//                        stack = stackTrace
//                    )
//                }
//                else -> {
//                    AppException(
//                        message = error.toString(),
//                        errorCode = ErrorCode.UNKNOWN_ERROR,
//                        error = error,
//                        stack = stackTrace
//                    )
//                }
//            }
//        }

        fun handleFirebaseAuthException(error: Exception, stackTrace: Array<StackTraceElement>): AppException {
            return when (error) {

                is FirebaseAuthInvalidCredentialsException -> {
                    Log.e("handleFirebaseAuthException",error.errorCode)
                    when (error.errorCode) {
                        "ERROR_INVALID_EMAIL" -> AppException(
                            title = ErrorCode.INVALID_EMAIL.title,
                            message = ErrorCode.INVALID_EMAIL.message,
                            errorCode = ErrorCode.INVALID_EMAIL,
                            error = error,
                            stack = stackTrace
                        )

                        "ERROR_WRONG_PASSWORD" -> AppException(
                            title = ErrorCode.INVALID_PASSWORD.title,
                            message = ErrorCode.INVALID_PASSWORD.message,
                            errorCode = ErrorCode.INVALID_PASSWORD,
                            error = error,
                            stack = stackTrace
                        )

                        "ERROR_INVALID_VERIFICATION_CODE", "ERROR_INVALID_VERIFICATION_ID" -> AppException(
                            title = ErrorCode.INVALID_ACTION_CODE.title,
                            message = ErrorCode.INVALID_ACTION_CODE.message,
                            errorCode = ErrorCode.INVALID_ACTION_CODE,
                            error = error,
                            stack = stackTrace
                        )

                        else -> AppException(
                            title = ErrorCode.INVALID_CREDENTIAL.title,
                            message = ErrorCode.INVALID_CREDENTIAL.message,
                            errorCode = ErrorCode.INVALID_CREDENTIAL,
                            error = error,
                            stack = stackTrace
                        )
                    }
                }

                // Very common: User not found
                is FirebaseAuthInvalidUserException -> {
                    when (error.errorCode) {
                        "ERROR_USER_NOT_FOUND" -> AppException(
                            title = ErrorCode.USER_NOT_FOUND.title,
                            message = ErrorCode.USER_NOT_FOUND.message,
                            errorCode = ErrorCode.USER_NOT_FOUND,
                            error = error,
                            stack = stackTrace
                        )

                        "ERROR_USER_DISABLED" -> AppException(
                            title = ErrorCode.USER_DISABLED.title,
                            message = ErrorCode.USER_DISABLED.message,
                            errorCode = ErrorCode.USER_DISABLED,
                            error = error,
                            stack = stackTrace
                        )

                        else -> AppException(
                            title = ErrorCode.USER_NOT_FOUND.title,
                            message = ErrorCode.USER_NOT_FOUND.message,
                            errorCode = ErrorCode.USER_NOT_FOUND,
                            error = error,
                            stack = stackTrace
                        )
                    }
                }

                // Common during registration: Email already exists
                is FirebaseAuthUserCollisionException -> {
                    when (error.errorCode) {
                        "ERROR_EMAIL_ALREADY_IN_USE", "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> AppException(
                            title = ErrorCode.EMAIL_ALREADY_IN_USE.title,
                            message = ErrorCode.EMAIL_ALREADY_IN_USE.message,
                            errorCode = ErrorCode.EMAIL_ALREADY_IN_USE,
                            error = error,
                            stack = stackTrace
                        )

                        else -> AppException(
                            title = ErrorCode.EMAIL_ALREADY_IN_USE.title,
                            message = ErrorCode.EMAIL_ALREADY_IN_USE.message,
                            errorCode = ErrorCode.EMAIL_ALREADY_IN_USE,
                            error = error,
                            stack = stackTrace
                        )
                    }
                }

                // Common during registration: Weak password
                is FirebaseAuthWeakPasswordException -> AppException(
                    title = ErrorCode.WEAK_PASSWORD.title,
                    message = error.reason?.let {
                        "Password is too weak. $it"
                    } ?: ErrorCode.WEAK_PASSWORD.message,
                    errorCode = ErrorCode.WEAK_PASSWORD,
                    error = error,
                    stack = stackTrace
                )

                // Common: Network connectivity issues
                is FirebaseNetworkException -> AppException(
                    title = ErrorCode.NO_INTERNET_CONNECTION.title,
                    message = ErrorCode.NO_INTERNET_CONNECTION.message,
                    errorCode = ErrorCode.NO_INTERNET_CONNECTION,
                    error = error,
                    stack = stackTrace
                )

                // Common: Too many failed attempts
                is FirebaseTooManyRequestsException -> AppException(
                    title = ErrorCode.TOO_MANY_REQUESTS.title,
                    message = ErrorCode.TOO_MANY_REQUESTS.message,
                    errorCode = ErrorCode.TOO_MANY_REQUESTS,
                    error = error,
                    stack = stackTrace
                )

                // Less common: Recent login required for sensitive operations
                is FirebaseAuthRecentLoginRequiredException -> AppException(
                    title = ErrorCode.RECENT_LOGIN_REQUIRED.title,
                    message = ErrorCode.RECENT_LOGIN_REQUIRED.message,
                    errorCode = ErrorCode.RECENT_LOGIN_REQUIRED,
                    error = error,
                    stack = stackTrace
                )

                // Less common: Email verification issues
                is FirebaseAuthEmailException -> AppException(
                    title = ErrorCode.EMAIL_NOT_VERIFIED.title,
                    message = ErrorCode.EMAIL_NOT_VERIFIED.message,
                    errorCode = ErrorCode.EMAIL_NOT_VERIFIED,
                    error = error,
                    stack = stackTrace
                )

                // Less common: Action code issues (password reset, email verification)
                is FirebaseAuthActionCodeException -> AppException(
                    title = ErrorCode.INVALID_ACTION_CODE.title,
                    message = ErrorCode.INVALID_ACTION_CODE.message,
                    errorCode = ErrorCode.INVALID_ACTION_CODE,
                    error = error,
                    stack = stackTrace
                )

                // Handle other FirebaseAuthException cases
                is FirebaseAuthException -> {
                    when (error.errorCode) {
                        // Common specific error codes
                        "ERROR_OPERATION_NOT_ALLOWED" -> AppException(
                            title = ErrorCode.OPERATION_NOT_ALLOWED.title,
                            message = ErrorCode.OPERATION_NOT_ALLOWED.message,
                            errorCode = ErrorCode.OPERATION_NOT_ALLOWED,
                            error = error,
                            stack = stackTrace
                        )

                        "ERROR_USER_TOKEN_EXPIRED", "ERROR_REQUIRES_RECENT_LOGIN" -> AppException(
                            title = ErrorCode.RECENT_LOGIN_REQUIRED.title,
                            message = ErrorCode.RECENT_LOGIN_REQUIRED.message,
                            errorCode = ErrorCode.RECENT_LOGIN_REQUIRED,
                            error = error,
                            stack = stackTrace
                        )
                        // Most common catch-all for other Firebase auth errors
                        else -> AppException(
                            title = ErrorCode.FIREBASE_ERROR.title,
                            message = error.message ?: ErrorCode.FIREBASE_ERROR.message,
                            errorCode = ErrorCode.FIREBASE_ERROR,
                            error = error,
                            stack = stackTrace
                        )
                    }
                }

                // Catch-all for non-Firebase exceptions
                else -> AppException(
                    title = ErrorCode.UNKNOWN_ERROR.title,
                    message = error.message ?: ErrorCode.UNKNOWN_ERROR.message,
                    errorCode = ErrorCode.UNKNOWN_ERROR,
                    error = error,
                    stack = stackTrace
                )
            }
        }

        private fun getDisplayMessageForStatusCode(statusCode: Int?, message: String?): String {
            return when (statusCode) {
                400 -> message ?: "[$statusCode] Invalid data. Please enter correct data"
                401 -> message ?: "[$statusCode] Authentication failed. Unauthorized user"
                403 -> message ?: "[$statusCode] You don't have permission to access this resource."
                404 -> message ?: "[$statusCode] The requested resource was not found."
                409 -> message ?: "[$statusCode] A conflict occurred with the current state of the resource."
                500, 501, 502, 503, 504, 505 -> message ?: "[$statusCode] A server error occurred. Please try again later."
                else -> message ?: "[$statusCode] An unexpected error occurred. Please try again."
            }
        }

        private fun getErrorCodeForStatusCode(statusCode: Int?): ErrorCode {
            return when (statusCode) {
                400 -> ErrorCode.BAD_REQUEST
                401 -> ErrorCode.UNAUTHORISED_ERROR
                403 -> ErrorCode.FORBIDDEN_ERROR
                404 -> ErrorCode.NOT_FOUND_ERROR
                409 -> ErrorCode.CONFLICT_ERROR
                500, 501, 502, 503, 504, 505 -> ErrorCode.INTERNAL_SERVER_ERROR
                else -> ErrorCode.API_ERROR
            }
        }

        fun removeStatusCode(input: String): String {
            val regex = Regex("\\[\\d+\\]\\s*")
            return input.replace(regex, "").trim()
        }
    }
}