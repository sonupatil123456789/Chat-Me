package com.example.mechat.presentation.home.viewmodel

import ChatModel
import com.example.mechat.core.resources.AppException
import com.example.mechat.domain.model.MessageModel
import com.example.mechat.domain.model.UserModel
import com.example.smartjobreminder.core.resources.CurrentViewState


data class HomeState(

    val chatListState: CurrentViewState = CurrentViewState.INITIAL,
    val userChatList : List<ChatModel> = emptyList<ChatModel>(),
    val userMessageList : List<MessageModel> = emptyList<MessageModel>(),
    val messageListState: CurrentViewState = CurrentViewState.INITIAL,
    val deleteUserChatList : List<ChatModel> = mutableListOf<ChatModel>(),
    val deleteUserMessageList : List<MessageModel> = mutableListOf<MessageModel>(),
    val error: AppException? = null,
    val selectedChat : ChatModel? = null

)


