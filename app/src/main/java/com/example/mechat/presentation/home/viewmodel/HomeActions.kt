package com.example.mechat.presentation.home.viewmodel

import ChatModel
import com.example.mechat.core.resources.AppException
import com.example.mechat.domain.model.MessageModel
import com.example.mechat.domain.model.UserModel
import com.example.mechat.presentation.auth.viewmodel.AuthUiEvent

sealed class HomeUiEvent {

    object GetAllChatsList : HomeUiEvent()
    data class GetAllMessageList(val chat : ChatModel,val onChatLoad : ()-> Unit ) : HomeUiEvent()
    data class SelectedChat(val selectedChat: ChatModel) : HomeUiEvent()

    data class DeleteSelectedChat(val selectedChat: ChatModel) : HomeUiEvent()
    data class DeleteSelectedMessage(val selectedMessage: MessageModel) : HomeUiEvent()
    class InitAndChangeChatStatus(val onSuccess : (ChatModel)-> Unit,val onError : (AppException)->Unit, val chat : ChatModel) : HomeUiEvent()
    class DeleteMultipleUserChat(val onSuccess : (List<ChatModel>)-> Unit,val onError : (AppException)->Unit) : HomeUiEvent()
    class CreateMessage(val onSuccess : (MessageModel)-> Unit, val onError : (AppException)->Unit, val message : MessageModel) : HomeUiEvent()
    class DeleteMultipleUserMessage(val onSuccess : (List<MessageModel>)-> Unit,val onError : (AppException)->Unit) : HomeUiEvent()


}

