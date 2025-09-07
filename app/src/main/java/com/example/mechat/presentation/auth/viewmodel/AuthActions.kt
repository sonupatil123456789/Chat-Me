package com.example.mechat.presentation.auth.viewmodel

import com.example.mechat.core.resources.AppException
import com.example.mechat.domain.model.UserModel

sealed class AuthUiEvent {
    data class EmailChanged(val email: String) : AuthUiEvent()
    data class PasswordChanged(val password: String) : AuthUiEvent()
    data class GetAllUserList(val search: String) : AuthUiEvent()
    data class UsernameChanged(val username: String) : AuthUiEvent()
    object TogglePasswordVisibility : AuthUiEvent()
    object ToggleAuthMode : AuthUiEvent()
    object CloseError : AuthUiEvent()
    class Login(val onSuccess : (UserModel)-> Unit,val onError : (AppException)->Unit) : AuthUiEvent()
    class Register(val onSuccess : (UserModel)-> Unit,val onError : (AppException)->Unit) : AuthUiEvent()
    class IsUserLoggedIn(val onSuccess : (Boolean, UserModel)-> Unit, val onError : (AppException)->Unit) : AuthUiEvent()
    class Logout(val onSuccess : (Boolean)-> Unit, val onError : (AppException)->Unit) : AuthUiEvent()
}

