package com.example.mechat.data.datasource.remote

import com.example.mechat.data.dto.ChatDto
import com.example.mechat.data.dto.MessageDto
import com.example.mechat.data.dto.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthDataSource {

    fun isUserLoggedIn(): FirebaseUser?

    suspend fun signIn(email: String, password: String): FirebaseUser

    suspend fun signeUp(email: String, password: String): FirebaseUser

    fun signOut(): Boolean

    suspend fun deleteAccount(): Boolean

}


interface UserDataSource {

    suspend fun createUser(chat : UserDto) : UserDto

    suspend fun deleteUserById (chat : UserDto) : UserDto

    suspend fun getUserById (chat : UserDto) : UserDto

    suspend fun getAllUsersList (myId : String, search  : String) : Flow<List<UserDto>>


}

interface ChatDataSource {



    suspend fun initAndChangeChatStatus (chat : ChatDto , userId : String) : ChatDto

    suspend fun deleteChatById (chat : ChatDto) : ChatDto

    suspend fun deleteMultipleChats(chatList : List<ChatDto>): List<ChatDto>

    suspend fun getChatById (chat : ChatDto) : ChatDto

    suspend fun getAllUserChatList(userId : String): Flow<List<ChatDto>>

    suspend fun createMessage (message : MessageDto) : MessageDto

    suspend fun getAllMessageList (chat : ChatDto) : Flow<List<MessageDto>>

    suspend fun deleteMultipleMessage(messageList : List<MessageDto>): List<MessageDto>


}

