package com.example.mechat.presentation.home.screens

import ChatModel
import UserAvatar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mechat.core.resources.formatTime
import com.example.mechat.core.resources.toCapitalise
import com.example.mechat.domain.model.UserModel
import com.example.mechat.presentation.auth.viewmodel.AuthState
import com.example.mechat.presentation.home.viewmodel.HomeState
import com.example.mechat.presentation.home.viewmodel.HomeUiEvent
import com.example.smartjobreminder.core.resources.CurrentViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// -------- HOME SCREEN --------
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    searchScreen : ()-> Unit,
    homeState : HomeState,
    authState : AuthState,
    onEvent : (HomeUiEvent)-> Unit = {},
    chatScreen : (ChatModel)-> Unit
) {

    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        onEvent(HomeUiEvent.GetAllChatsList)
    }




    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") },
                actions = {
                    IconButton(onClick = searchScreen) {
                        Icon(
                            imageVector =  Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
            })},

    ) { padding ->


        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {

            if(homeState.chatListState == CurrentViewState.SUCCESS){
                if(homeState.userChatList.isEmpty()){
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(3f).fillMaxWidth()
                    ){
                        Text("No Chat")
                    }
                }else {
                    LazyColumn (){
                        items(homeState.userChatList.size) { chatIndex ->
                            val chat = homeState.userChatList[chatIndex];
                            ChatItem(chat,
                                authState = authState,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(12.dp).clickable{
                                        onEvent( HomeUiEvent.InitAndChangeChatStatus(
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
            else if(homeState.chatListState == CurrentViewState.ERROR){
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(3f).fillMaxWidth()
                ){
                    Text(homeState.error?.message ?: "")
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





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatItem(chat: ChatModel, modifier: Modifier, authState : AuthState) {
    val headerData = chat.participants.first {
        it.id != authState.currentUser?.id
    }

    Row(
        modifier = modifier.height(50.dp)
    ) {
        UserAvatar(
            modifier = Modifier,
            imageUrl = null,
        )

        Spacer(modifier = Modifier.width(10.dp))
        Column (modifier = Modifier.weight(4.0f).height(50.dp)){
            Text(headerData.name.toCapitalise() , fontWeight = FontWeight.Bold,color = Color.Black,fontSize = 14.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (chat.participants.any { it.readed == 0 }) Icons.Default.Done else Icons.Default.DoneAll,
                    tint = if (chat.participants.any { it.readed == 0 }) Color.Gray else Color.Blue,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(chat.lastMessage ?: "", color = Color.Gray, fontSize = 12.sp, overflow = TextOverflow.Ellipsis)
            }


        }
        Spacer(modifier = Modifier.width(10.dp))

        Column (
            modifier = Modifier
                .fillMaxHeight().padding(bottom = 10.dp),
             verticalArrangement = Arrangement.Bottom

        ) {
            Text(chat.lastMessageTime?.time?.formatTime().toString(),
                style = TextStyle(color = Color.Gray, fontSize = 12.sp),
            )
        }
    }
}