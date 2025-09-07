package com.example.mechat.presentation.home.screens

import ChatModel
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mechat.core.components.MessageInputBox
import com.example.mechat.core.resources.formatTime
import com.example.mechat.core.resources.toCapitalise
import com.example.mechat.domain.model.MessageModel
import com.example.mechat.presentation.auth.viewmodel.AuthState
import com.example.mechat.presentation.home.viewmodel.HomeState
import com.example.mechat.presentation.home.viewmodel.HomeUiEvent
import com.example.smartjobreminder.core.resources.CurrentViewState
import com.example.smartjobreminder.core.resources.MessageSide
import com.example.smartjobreminder.core.resources.MessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date


// -------- MESSAGE SCREEN --------
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    goBack : ()-> Unit,
    homeState : HomeState,
    homeEvent : (HomeUiEvent)-> Unit = {},
    chatData : ChatModel,
    authState : AuthState
) {

    var messageText by remember { mutableStateOf<String>("") }

    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()


    LaunchedEffect(Unit) {
        homeEvent(HomeUiEvent.GetAllMessageList(
            chatData,
            onChatLoad = {
                scope.launch(Dispatchers.Main) {
                    listState.scrollToItem(homeState.userMessageList.size)
                }
            }
        ))

    }




    val headerData = remember(chatData, authState.currentUser?.id) {
        chatData.participants.first { it.id != authState.currentUser?.id }
    }




    Scaffold(
        topBar = {
            TopAppBar(title = { Text(headerData.name.toCapitalise(),style = TextStyle(color = Color.Gray, fontSize = 18.sp, fontWeight = FontWeight.W400),) },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                )},

    ) { padding ->


        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
        ) {


            if(homeState.messageListState == CurrentViewState.SUCCESS){
                if(homeState.userMessageList.isEmpty()){
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(3f).fillMaxWidth()
                    ){
                        Text("No Message")
                    }
                }else {
                    LazyColumn (
                        state = listState
                    ){

                        items(homeState.userMessageList.size) { chatIndex ->
                            val message = homeState.userMessageList[chatIndex];
                            MessageItem(message, Modifier)
                        }
                    }
                }
            }
            else if(homeState.messageListState == CurrentViewState.ERROR){
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


            MessageInputBox(
                modifier = Modifier,
                onSendClick = {

                    val timeStamp = Date()

                    scope.launch(Dispatchers.IO) {

                        homeEvent(HomeUiEvent.CreateMessage(
                            onSuccess = {
                                keyboardController?.hide()
                                messageText = ""
                            },
                            onError = {},
                            message = MessageModel(
                                type = MessageType.TEXT,
                                text = messageText,
                                timestamp = timeStamp,
                                chatId = chatData.id,
                                receiverId = mutableListOf(headerData.id),
                                senderId = authState.currentUser?.id ?: ""
                            )
                        ))

                    }

                },
                messageText = messageText,
                onMessageChange = {
                    messageText = it
                }
            )


        }



        }

}





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageItem(
    message: MessageModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (message.messageSide == MessageSide.RIGHT) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = modifier
                .background(
                    color = if (message.messageSide == MessageSide.RIGHT) Color(0xFFDCF8C6) else Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .width(IntrinsicSize.Max)
                .widthIn(min = 60.dp, max = 280.dp)
        ) {
            // Message text
            Text(
                text = message.text,
                style = TextStyle(color = Color.Black, fontSize = 16.sp),
                modifier = Modifier.padding(bottom = 2.dp)
            )

            // Timestamp + status icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (message.messageSide == MessageSide.RIGHT) Arrangement.End else Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.timestamp.time.formatTime().toString(),
                    style = TextStyle(color = Color.Gray, fontSize = 10.sp),
                    modifier = Modifier.padding(end = 4.dp)
                )
                if (message.messageSide == MessageSide.RIGHT){
                    Icon(
                        imageVector = if (!message.isDelivered) Icons.Default.Done else Icons.Default.DoneAll,
                        tint = if (!message.isDelivered) Color.Gray else Color.Blue,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }

            }
        }
    }
}
