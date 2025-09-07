package com.example.mechat.presentation.auth.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mechat.core.components.ErrorBar
import com.example.mechat.presentation.auth.viewmodel.AuthState
import com.example.mechat.presentation.auth.viewmodel.AuthUiEvent
import com.example.smartjobreminder.core.resources.CurrentViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val TAG = "LoginScreen"
// -------- LOGIN SCREEN --------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin : (Boolean) -> Unit,
    signeUpScreen : ()-> Unit,
    onEvent : (AuthUiEvent)-> Unit = {},
    authState : AuthState = AuthState()
) {


    val scope = rememberCoroutineScope()


    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {


            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().height(80.dp)){
                if(authState.error != null ){
                    ErrorBar(message = authState.error.message){
                        onEvent(AuthUiEvent.CloseError)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))



            OutlinedTextField(
                value = authState.email,
                onValueChange = { onEvent(AuthUiEvent.EmailChanged(it)) },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = authState.password,
                onValueChange = { onEvent(AuthUiEvent.PasswordChanged(it)) },
                label = { Text("Password") },
                trailingIcon = {
                    if (authState.isPasswordVisible){
                        Icon(
                            imageVector = Icons.Outlined.Visibility ,
                            contentDescription = "Visibility Icon",
                            modifier = Modifier.clickable{
                                onEvent(AuthUiEvent.TogglePasswordVisibility)
                            }
                        )
                    }else {
                        Icon(
                            imageVector = Icons.Outlined.VisibilityOff ,
                            contentDescription = "VisibilityOff Icon",
                            modifier = Modifier.clickable{
                                onEvent(AuthUiEvent.TogglePasswordVisibility)
                            }
                        )
                    }


                },
                singleLine = true,
                visualTransformation = if(authState.isPasswordVisible) { VisualTransformation.None} else {PasswordVisualTransformation()},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))


            Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().height(50.dp)){

                if (authState.appState == CurrentViewState.LOADING){

                    CircularProgressIndicator()

                }else {
                    Button(
                        onClick = {
                            onEvent(AuthUiEvent.Login(
                                onSuccess = { user ->
                                    scope.launch(Dispatchers.Main) {
                                        onLogin(true)
                                    }
                                },
                                onError = { error ->
                                    Log.e(TAG,error.toString())
                                }
                            ))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Login")
                    }
                }

            }

            Spacer(modifier = Modifier.height(10.dp))





            TextButton(
                onClick =  signeUpScreen,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Don’t have an account? Sign Up")
            }
        }
    }
}