package com.example.mechat.data.repositoryimpl

import ChatModel
import android.util.Log
import com.example.mechat.core.resources.AppException
import com.example.mechat.core.resources.DataResult
import com.example.mechat.data.datasource.remote.AuthDataSource
import com.example.mechat.data.datasource.remote.ChatDataSource
import com.example.mechat.data.datasource.remote.ChatFirebaseDataSourceImpl
import com.example.mechat.data.datasource.remote.UserDataSource
import com.example.mechat.data.dto.ChatDto
import com.example.mechat.data.dto.MessageDto
import com.example.mechat.domain.model.MessageModel
import com.example.mechat.domain.model.UserModel
import com.example.mechat.domain.repository.ChatRepository
import com.example.smartjobreminder.core.resources.MessageSide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.collections.map


class ChatRepositoryImpl @Inject constructor(


    private  val chatDataSource: ChatDataSource,
    private val firebaseAuth: FirebaseAuth,


    ): ChatRepository {

    private val  TAG = "ChatRepositoryImpl"

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid



    override suspend fun initAndChangeChatStatus(chat: ChatModel): DataResult<ChatModel> {
        try {
            val result = chatDataSource.initAndChangeChatStatus(chat.toFirebaseModel(),currentUserId ?: "")
            return  DataResult.Success<ChatModel>( "Success" , result.toDomain())

        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<ChatModel>(e)
        }
    }

    override suspend fun getAllUserChatList(): DataResult<Flow<List<ChatModel>>> {
        try {
            return  DataResult.Success<Flow<List<ChatModel>>>( "Success",
                chatDataSource.getAllUserChatList(currentUserId ?: "").map { it.map { it.toDomain() }
                })

        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<Flow<List<ChatModel>>>(e)
        }
    }

    override suspend fun deleteMultipleChats(chatList: List<ChatModel>): DataResult<List<ChatModel>> {
        try {
            val result = chatDataSource.deleteMultipleChats(chatList.map { it.toFirebaseModel() })
            return  DataResult.Success<List<ChatModel>>( "Success" , result.map { it.toDomain() })

        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<List<ChatModel>>(e)
        }
    }

    override suspend fun createMessage(message: MessageModel): DataResult<MessageModel> {
        try {
            val result = chatDataSource.createMessage(message.toFirebaseModel())
            return  DataResult.Success<MessageModel>( "Success" , result.toDomain())

        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<MessageModel>(e)
        }
    }

    override suspend fun getAllMessageList(chat: ChatModel): DataResult<Flow<List<MessageModel>>> {
        try {

            val messageList =  chatDataSource.getAllMessageList(chat.toFirebaseModel())

            return  DataResult.Success<Flow<List<MessageModel>>>( "Success",
                messageList.map { it.map { message ->
                    if(message.senderId == currentUserId){
                        return@map message.toDomain().copy(messageSide = MessageSide.RIGHT)
                    }else {
                        return@map message.toDomain().copy(messageSide = MessageSide.LEFT)
                    }
                } }
            )

        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<Flow<List<MessageModel>>>(e)
        }
    }

    override suspend fun deleteMultipleMessage(messageList: List<MessageModel>): DataResult<List<MessageModel>> {
        try {
            val result = chatDataSource.deleteMultipleMessage(messageList.map { it.toFirebaseModel() })
            return  DataResult.Success<List<MessageModel>>( "Success" , result.map { it.toDomain() })

        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<List<MessageModel>>(e)
        }
    }


}