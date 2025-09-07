package com.example.mechat.domain.repository

import ChatModel
import com.example.mechat.core.resources.DataResult
import com.example.mechat.data.dto.ChatDto
import com.example.mechat.data.dto.MessageDto
import com.example.mechat.domain.model.MessageModel
import com.example.mechat.domain.model.UserModel
import kotlinx.coroutines.flow.Flow


interface ChatRepository {



    suspend fun initAndChangeChatStatus(chat : ChatModel): DataResult<ChatModel>

    suspend fun getAllUserChatList():DataResult<Flow<List<ChatModel>>>

    suspend fun deleteMultipleChats(chatList : List<ChatModel>): DataResult<List<ChatModel>>

    suspend fun createMessage (message : MessageModel) : DataResult<MessageModel>

    suspend fun getAllMessageList (chat : ChatModel) : DataResult<Flow<List<MessageModel>>>

    suspend fun deleteMultipleMessage(messageList : List<MessageModel>): DataResult<List<MessageModel>>

}