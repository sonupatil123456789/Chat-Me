package com.example.mechat.presentation.auth.viewmodel

import com.example.mechat.core.resources.AppException
import com.example.mechat.domain.model.UserModel
import com.example.smartjobreminder.core.resources.CurrentViewState


data class AuthState(
    val appState: CurrentViewState = CurrentViewState.INITIAL,
    val userListState: CurrentViewState = CurrentViewState.INITIAL,
    val userList : List<UserModel> = emptyList<UserModel>(),
    val isLoggedIn: Boolean = false,
    val currentUser: UserModel? = null,
    val error: AppException? = null,
    val isPasswordVisible: Boolean = false,
    val email: String = "",
    val password: String = "",
    val username: String = "",
    val isRegisterMode: Boolean = false,
    var isFormValidated: Boolean = false,

)


