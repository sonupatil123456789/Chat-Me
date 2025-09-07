package com.example.mechat.presentation.home.screens

import ChatModel
import ParticipantModel
import UserAvatar
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mechat.core.components.SearchBar
import com.example.mechat.core.resources.toCapitalise
import com.example.mechat.domain.model.UserModel
import com.example.mechat.presentation.auth.viewmodel.AuthState
import com.example.mechat.presentation.auth.viewmodel.AuthUiEvent
import com.example.mechat.presentation.home.viewmodel.HomeUiEvent
import com.example.smartjobreminder.core.resources.CurrentViewState
import com.example.smartjobreminder.core.resources.MessageType
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch



// -------- SEARCH SCREEN --------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    chatScreen : (ChatModel)-> Unit,
    goBack : ()-> Unit,
    onEvent : (AuthUiEvent)-> Unit = {},
    authState : AuthState = AuthState(),
    homeEvent : (HomeUiEvent)-> Unit = {}
) {

    var searchText by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()


    LaunchedEffect(searchText) {
        snapshotFlow { searchText }
            .debounce(500)
            .distinctUntilChanged()
            .collect { query ->
                onEvent(AuthUiEvent.GetAllUserList(query))
            }
    }





    Scaffold(
        topBar = {
            TopAppBar(title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            SearchBar(
                query = searchText,
                onQueryChange = {
                    searchText = it
                },
                placeholder = "Search..."
            )

            if(authState.userListState == CurrentViewState.SUCCESS){
                if(authState.userList.isEmpty()){
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(3f).fillMaxWidth()
                    ){
                        Text("No User")
                    }
                }else {
                    LazyColumn (modifier = Modifier.padding(16.dp)){
                        items(authState.userList.size) { userIndex ->
                            val user = authState.userList[userIndex];
                            UserCard(user ,
                                Modifier.fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable{
                                        val chat = ChatModel(
                                            type = MessageType.NULL,
                                            participantsIds =mutableListOf(user.id ,authState.currentUser!!.id),
                                            participants = mutableListOf(
                                                ParticipantModel(user.id,user.userName,0),
                                                ParticipantModel(authState.currentUser!!.id,authState.currentUser!!.userName,0)
                                            )
                                        )
                                        homeEvent( HomeUiEvent.InitAndChangeChatStatus(
                                            onSuccess = {
                                                scope.launch (Dispatchers.Main){
                                                    chatScreen(it)
                                                }
                                            },
                                            onError = {},
                                            chat = chat
                                        ))
                                    }
                            )
                        }
                    }
                }
            }
            else if(authState.userListState == CurrentViewState.ERROR){
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(3f).fillMaxWidth()
                ){
                    Text(authState.error?.message ?: "")
                }
            }
            else {
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(3f).fillMaxWidth()
                ){
                    CircularProgressIndicator()
                }
            }
        }
    }
}



@Composable
fun UserCard(user: UserModel, modifier : Modifier) {
    Row(
        modifier = modifier
    ) {
        UserAvatar(
            modifier = Modifier,
            imageUrl = user.profileImageUrl,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(user.userName.toCapitalise() , fontWeight = FontWeight.Bold,color = Color.Black,fontSize = 14.sp)


            Text(user.email, color = Color.Gray, fontSize = 12.sp)


        }
    }
}