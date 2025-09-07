package com.example.mechat.presentation.auth.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mechat.core.resources.AppException
import com.example.mechat.core.resources.DataResult
import com.example.mechat.domain.model.UserModel
import com.example.mechat.domain.repository.AuthRepository
import com.example.smartjobreminder.core.resources.CurrentViewState
import com.example.smartjobreminder.core.resources.ErrorCode
import com.example.smartjobreminder.core.resources.UserStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(){


    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()



    fun onAction(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.EmailChanged -> updateEmail(event.email)
            is AuthUiEvent.PasswordChanged -> updatePassword(event.password)
            is AuthUiEvent.UsernameChanged -> updateUsername(event.username)
            is AuthUiEvent.TogglePasswordVisibility -> togglePasswordVisibility()
            is AuthUiEvent.ToggleAuthMode -> toggleAuthMode()
            is AuthUiEvent.Login -> logIn(event.onSuccess, event.onError)
            is AuthUiEvent.Logout -> logOut(event.onSuccess, event.onError)
            is AuthUiEvent.Register -> signeUp(event.onSuccess, event.onError)
            AuthUiEvent.CloseError -> closeError()
            is AuthUiEvent.GetAllUserList -> getAllUserList(event.search)
            is AuthUiEvent.IsUserLoggedIn -> isUserLoggedIn(event.onSuccess, event.onError)
        }
    }




    private fun closeError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }

    private fun updateEmail(email: String) {
        Log.d("updateEmail",email)
        _uiState.value = _uiState.value.copy(
            email = email,
        )
    }

    private fun updatePassword(password: String) {
        Log.d("updatePassword",password)
        _uiState.value = _uiState.value.copy(
            password = password,
        )
    }

    private fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username,
        )
    }

    private fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    private fun toggleAuthMode() {
        _uiState.value = _uiState.value.copy(
            isRegisterMode = !_uiState.value.isRegisterMode,
            error = null,
        )
    }

    private fun getAllUserList(search: String) {
        _uiState.update {  it.copy(userListState = CurrentViewState.LOADING ) }
        viewModelScope.launch(Dispatchers.IO){
            val dataResult : DataResult<Flow<List<UserModel>>> = authRepository.getAllUsersList(search)
            when(dataResult) {
                is DataResult.Error<Flow<List<UserModel>>> -> {
                    _uiState.update {
                        it.copy(error = dataResult.error, userListState = CurrentViewState.ERROR )
                    }
                }
                is DataResult.Success<Flow<List<UserModel>>> -> {
                    dataResult.data?.collect { userList ->

                        _uiState.update {
                            it.copy(userList = userList ,userListState = CurrentViewState.SUCCESS )
                        }
                    }
                }
            }
        }
    }


    private fun validateLoginInput() : Boolean{
        val emailError = when {
            _uiState.value.email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches() -> "Invalid email format"
            else -> null
        }

        val passwordError = when {
            _uiState.value.password.isBlank() -> "Password is required"
            _uiState.value.password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }


        if (emailError != null){
            _uiState.value = _uiState.value.copy(
                isFormValidated = false,
                error = AppException(
                    title = ErrorCode.INVALID_EMAIL.title,
                    message = emailError,
                    errorCode = ErrorCode.INVALID_EMAIL,
                    stack = arrayOf()
                ),
            )
            return false ;
        }
        if (passwordError != null){
            _uiState.value = _uiState.value.copy(
                isFormValidated = false,
                error = AppException(
                    title = ErrorCode.INVALID_PASSWORD.title,
                    message = passwordError,
                    errorCode = ErrorCode.INVALID_PASSWORD,
                    stack = arrayOf()
                ),
            )
            return false ;
        }
        else {
            _uiState.value = _uiState.value.copy(
                isFormValidated = true,
                error = null,
            )
            return true ;
        }
    }

    private fun logIn(onSuccess : (UserModel)-> Unit, onError : (AppException)->Unit) {

        validateLoginInput()

        if(_uiState.value.isFormValidated){
            val input =  UserModel(
                password = _uiState.value.password,
                email = _uiState.value.email,
                createdAt = Date(),
                isOnline = true,
                status = UserStatus.ONLINE
            )
            _uiState.value = _uiState.value.copy(appState = CurrentViewState.LOADING )
            viewModelScope.launch(Dispatchers.IO){
                val dataResult : DataResult<UserModel> = authRepository.signIn(input)
                when(dataResult) {
                    is DataResult.Error<UserModel> -> {
                        _uiState.value = _uiState.value.copy(error = dataResult.error, appState = CurrentViewState.ERROR,isLoggedIn = false )
                        onError(dataResult.error!!)
                    }
                    is DataResult.Success<UserModel> -> {
                        _uiState.value = _uiState.value.copy(appState = CurrentViewState.SUCCESS, currentUser = dataResult.data,isLoggedIn = true)
                        onSuccess(dataResult.data!!)
                    }

                }
            }

        }

    }

    private fun validateSigneUpInput() : Boolean{
        val emailError = when {
            _uiState.value.email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches() -> "Invalid email format"
            else -> null
        }

        val passwordError = when {
            _uiState.value.password.isBlank() -> "Password is required"
            _uiState.value.password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }


        if (emailError != null){
            _uiState.value = _uiState.value.copy(
                isFormValidated = false,
                error = AppException(
                    title = ErrorCode.INVALID_EMAIL.title,
                    message = emailError,
                    errorCode = ErrorCode.INVALID_EMAIL,
                    stack = arrayOf()
                ),
            )
            return false ;
        }
        if (passwordError != null){
            _uiState.value = _uiState.value.copy(
                isFormValidated = false,
                error = AppException(
                    title = ErrorCode.INVALID_PASSWORD.title,
                    message = passwordError,
                    errorCode = ErrorCode.INVALID_PASSWORD,
                    stack = arrayOf()
                ),
            )
            return false ;
        }
         if (_uiState.value.password.isBlank()){
            _uiState.value = _uiState.value.copy(
                isFormValidated = false,
                error = AppException(
                    title = ErrorCode.DEFAULT.title,
                    message = "Name should not be empty",
                    errorCode = ErrorCode.DEFAULT,
                    stack = arrayOf()
                ),
            )
            return false ;
        }
        else {
            _uiState.value = _uiState.value.copy(
                isFormValidated = true,
                error = null,
            )
            return true ;
        }
    }

    private fun signeUp(onSuccess : (UserModel)-> Unit, onError : (AppException)->Unit) {

        validateSigneUpInput()

        if(_uiState.value.isFormValidated){
            val input =  UserModel(
                userName = _uiState.value.username,
                password = _uiState.value.password,
                email = _uiState.value.email,
                createdAt = Date(),
                isOnline = true,
                status = UserStatus.ONLINE
            )
            _uiState.value = _uiState.value.copy(appState = CurrentViewState.LOADING )
            viewModelScope.launch (Dispatchers.IO){
                val dataResult : DataResult<UserModel> = authRepository.signeUp(input)
                when(dataResult) {
                    is DataResult.Error<UserModel> -> {
                        _uiState.value = _uiState.value.copy(error = dataResult.error, appState = CurrentViewState.ERROR, isLoggedIn = false )
                        onError(dataResult.error!!)
                    }
                    is DataResult.Success<UserModel> -> {
                        _uiState.value = _uiState.value.copy(appState = CurrentViewState.SUCCESS, currentUser = dataResult.data,isLoggedIn = true)
                        onSuccess(dataResult.data!!)
                    }

                }
            }
        }

    }

    fun logOut(onSuccess : (Boolean)-> Unit, onError : (AppException)->Unit){
        val dataResult : DataResult<Boolean> = authRepository.signOut()

        when(dataResult) {
            is DataResult.Error<Boolean> -> {
                onError(dataResult.error!!)
            }
            is DataResult.Success<Boolean> -> {
                _uiState.value = AuthState()
                onSuccess(true)
            }
        }


    }

    private fun isUserLoggedIn(onSuccess : (Boolean,UserModel)-> Unit, onError : (AppException)->Unit) {

        viewModelScope.launch(Dispatchers.IO){
            val dataResult : DataResult<UserModel?> = authRepository.isUserLoggedIn()
            when(dataResult) {
                is DataResult.Error<UserModel?> -> {
                    _uiState.value = _uiState.value.copy(appState = CurrentViewState.ERROR,isLoggedIn = false )
                    onError(dataResult.error!!)
                }
                is DataResult.Success<UserModel?> -> {
                    _uiState.value = _uiState.value.copy(appState = CurrentViewState.SUCCESS, currentUser = dataResult.data, isLoggedIn = true)
                    onSuccess(true ,dataResult.data!!)
                }

            }
        }


    }


}