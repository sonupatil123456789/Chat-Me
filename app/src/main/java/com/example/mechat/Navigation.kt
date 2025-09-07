package com.example.mechat


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mechat.presentation.auth.screens.LoginScreen
import com.example.mechat.presentation.auth.screens.SigneUpScreen
import com.example.mechat.presentation.auth.viewmodel.AuthUiEvent
import com.example.mechat.presentation.auth.viewmodel.AuthViewModel
import com.example.mechat.presentation.home.screens.ChatScreen
import com.example.mechat.presentation.home.screens.HomeScreen
import com.example.mechat.presentation.home.screens.SearchScreen
import com.example.mechat.presentation.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf


@Serializable
object Routes {

    @Serializable
    data class Chat( val chatData : ChatNavModel)

    @Serializable
    object Splash

    @Serializable
    object Home

    @Serializable
    data class Profile(val id: String)

    @Serializable
    object Login

    @Serializable
    object SigneUp

    @Serializable
    object Search


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(){



    val navigationController = rememberNavController();

    val authViewModel : AuthViewModel = hiltViewModel()

    val homeViewModel : HomeViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        authViewModel.onAction(AuthUiEvent.IsUserLoggedIn(
            onError = {
                scope.launch(Dispatchers.Main) {
                    delay(1000)
                    navigationController.navigate(Routes.Login) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                }
            },
            onSuccess = {isLogin ,user ->
                scope.launch(Dispatchers.Main) {
                    delay(1000)
                    navigationController.navigate(Routes.Home) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                }
            }
        ))
    }


    NavHost(
        navController = navigationController,
        startDestination = Routes.Splash
    ){

        composable<Routes.Splash>(){
            SplashScreen()
        }





        signeUpScreen(navigationController, authViewModel)
        loginScreen(navigationController, authViewModel)
        homeScreen(navigationController,homeViewModel,authViewModel)
        searchScreen(navigationController, authViewModel,homeViewModel)
        chatScreen(navigationController,homeViewModel,authViewModel)

    }

}



fun NavGraphBuilder.homeScreen(navigationController: NavHostController ,homeViewModel:  HomeViewModel,authViewModel : AuthViewModel){
    composable<Routes.Home>(){
        val homeState by homeViewModel.uiState.collectAsState()
        val authState by authViewModel.uiState.collectAsState()

        HomeScreen(
            homeState =homeState ,
            onEvent =homeViewModel::onAction ,
            authState = authState,
            searchScreen = {
                navigationController.navigate(Routes.Search)
            },
            chatScreen = { chat->
                navigationController.navigate(Routes.Chat(ChatNavModel.toNavigationModel(chat)))
            }
        )

    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.chatScreen(navigationController: NavHostController, homeViewModel:  HomeViewModel, authViewModel : AuthViewModel){
    composable<Routes.Chat>(
      typeMap = mapOf(
          typeOf<ChatNavModel>() to CustomNavType.ChatNavType
      )
    ){

        val args = it.toRoute<Routes.Chat>()


        val homeState by homeViewModel.uiState.collectAsState()
        val authState by authViewModel.uiState.collectAsState()


        ChatScreen(
            goBack = {
                navigationController.popBackStack()
            },
            authState = authState,
            homeState = homeState,
            homeEvent = homeViewModel::onAction,
            chatData = ChatNavModel.fromNavigationModel(args.chatData)
        )
    }

}


fun NavGraphBuilder.searchScreen(navigationController: NavHostController,authViewModel : AuthViewModel,homeViewModel:  HomeViewModel){
    composable<Routes.Search>(){

        val authState by authViewModel.uiState.collectAsState()


        SearchScreen(
            chatScreen = { chat ->
                val args = ChatNavModel.toNavigationModel(chat);
                Log.d("chat ", chat.toString())
                Log.d("ARGUMENTS ", args.toString())
                navigationController.navigate(Routes.Chat(args))
            },
            goBack = {
                navigationController.popBackStack()
            },
            authState = authState,
            homeEvent = homeViewModel::onAction,
            onEvent = authViewModel::onAction

        )
    }

}

fun NavGraphBuilder.loginScreen(navigationController: NavHostController,authViewModel : AuthViewModel){
    composable<Routes.Login>(){

        val authState by authViewModel.uiState.collectAsState()



        LoginScreen(
            onLogin = {
                navigationController.navigate(Routes.Home)
            },
            signeUpScreen = {
                navigationController.navigate(Routes.SigneUp)
            },
            authState = authState,
            onEvent = authViewModel::onAction
        )

    }
}
fun NavGraphBuilder.signeUpScreen(navigationController: NavHostController,authViewModel : AuthViewModel){
    composable<Routes.SigneUp>(){

        val authState by authViewModel.uiState.collectAsState()


        SigneUpScreen(
            onSigneUp = {
                navigationController.navigate(Routes.Home)
            },
            goBack = {
                navigationController.popBackStack()
            },
            authState = authState,
            onEvent = authViewModel::onAction

        )

    }
}
