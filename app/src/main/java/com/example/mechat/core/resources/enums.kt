package com.example.smartjobreminder.core.resources

import kotlinx.serialization.Serializable


enum class CurrentViewState{
  INITIAL,
  LOADING,
  SUCCESS,
  ERROR
}


@Serializable
enum class MessageType (val title : String){
  NULL("NULL"),
  TEXT("TEXT"),
  IMAGE("IMAGE"),
  FILE("FILE"),
  AUDIO("AUDIO"),
  VIDEO("VIDEO"),
}

enum class MessageSide{
  RIGHT,
  LEFT,
  CENTER
}


enum class UserStatus {
  ONLINE,
  OFFLINE,
  AWAY,
  BUSY
}

enum class ErrorCode(val title: String, val message: String) {
  SUCCESS("Success", "Data Fetched Successfully"),
  DATABASE_ERROR("Database Error", "An unexpected error occurred with the local database"),
  API_ERROR("API Error", "Api Error"),
  NO_CONTENT("No Content", "No Content"),
  BAD_REQUEST("Bad Request", "Bad Request"),
  FORBIDDEN_ERROR("Access Denied", "Access Denied"),
  CONFLICT_ERROR("Conflict Error", "Conflict Error"),
  UNAUTHORISED_ERROR("Unauthorized", "Unauthorized User"),
  NOT_FOUND_ERROR("Not Found", "No Data Found"),
  INTERNAL_SERVER_ERROR("Server Error", "Internal Server Error"),
  CONNECT_TIMEOUT("Connection Timeout", "Connection Timeout"),
  CANCEL("Cancelled", "Operation was cancelled"),
  RECEIVE_TIMEOUT("Receive Timeout", "Receive Timeout"),
  SEND_TIMEOUT("Send Timeout", "Send Timeout"),
  CACHE_ERROR("Cache Error", "Cache operation failed"),
  NO_INTERNET_CONNECTION("No Internet", "No Internet Connection"),
  DEFAULT("Unknown Error", "Unknown Error"),
  UNKNOWN_ERROR("Unknown Error", "Unknown Error"),
  FIREBASE_ERROR("Authentication Error", "Firebase authentication failed"),
  INVALID_DATA_ERROR("Invalid Data", "Invalid Data"),

  // Firebase Auth specific error codes
  INVALID_EMAIL("Invalid Email", "The email address is invalid. Please enter a valid email address"),
  INVALID_CREDENTIAL("Invalid Credentials", "The email id or password is incorrect"),
  INVALID_PASSWORD("Invalid Password", "The password is incorrect. Please check your password and try again"),
  USER_NOT_FOUND("User Not Found", "No account found with this email address. Please check your email or create a new account"),
  EMAIL_ALREADY_IN_USE("Email Already Used", "An account with this email address already exists. Please sign in instead"),
  WEAK_PASSWORD("Weak Password", "Password is too weak. Please choose a stronger password with at least 6 characters"),
  RECENT_LOGIN_REQUIRED("Login Required", "This operation requires recent authentication. Please sign in again and try again"),
  TOO_MANY_REQUESTS("Too Many Requests", "Too many requests have been made. Please wait a moment and try again"),
  NETWORK_ERROR("Network Error", "Network error occurred. Please check your internet connection and try again"),
  EMAIL_NOT_VERIFIED("Email Not Verified", "Email verification required. Please check your email and verify your account"),
  INVALID_ACTION_CODE("Invalid Code", "The verification code is invalid or has expired. Please request a new one"),
  OPERATION_NOT_ALLOWED("Operation Not Allowed", "This sign-in method is not enabled. Please contact support or try a different method"),
  USER_DISABLED("Account Disabled", "Your account has been disabled. Please contact support for assistance")
}




