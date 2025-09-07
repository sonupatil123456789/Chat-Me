package com.example.mechat.data.datasource.remote



import android.util.Log
import com.example.mechat.data.dto.ChatDto
import com.example.mechat.data.dto.MessageDto
import com.example.mechat.data.dto.ParticipantDto
import com.example.mechat.data.dto.UserDto
import com.example.mechat.presentation.auth.screens.LoginScreen
import com.google.common.base.Objects
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class ChatFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatDataSource {

    companion object {
        private const val CHATS_COLLECTION = "chats"
        private const val MESSAGE_COLLECTION = "message"
        private val TAG = "ChatFirebaseDataSourceImpl";
    }


    override suspend fun initAndChangeChatStatus(chat: ChatDto, userId: String): ChatDto {
        try {
            val query = firestore.collection(CHATS_COLLECTION)
                .whereArrayContains("participantsIds", userId)
                .get()
                .await()

            // Find if chat with same participants already exists
            val chatDoc = query.documents.firstOrNull { doc ->
                val participants = doc.get("participantsIds") as? List<*>
                chat.participantsIds.all { participants?.contains(it) == true }
            }

            return if (chatDoc == null) {
                // Chat not present -> create new one

                firestore.collection(CHATS_COLLECTION)
                    .document(chat.id)
                    .set(chat)
                    .await()
                chat
            } else {
                // Chat exists -> update status

                val batch = firestore.batch()
                val existingDoc = chatDoc
                val updatedChat = existingDoc.toObject(ChatDto::class.java)

                // Update all messages sent by this user in this chat as delivered
                val querySnapshot = firestore.collection(MESSAGE_COLLECTION)
                    .whereEqualTo("chatId", existingDoc.id)
                    .whereNotEqualTo("senderId", userId)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val docRef = firestore.collection(MESSAGE_COLLECTION).document(document.id)
                    batch.update(docRef, "isDelivered", true)
                }

                // Update participants read status
                val updateParticipants = updatedChat?.participants?.map {
                    if (it.id == userId) {
                        it.copy(readed = 1)
                    } else {
                        it
                    }
                } ?: emptyList()

                val updateMap = mapOf(
                    "participants" to updateParticipants
                )

                firestore.collection(CHATS_COLLECTION)
                    .document(existingDoc.id)
                    .update(updateMap)
                    .await()

                batch.commit().await()

                updatedChat ?: chat
            }

        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e, e.stackTrace)
        }
    }









    override suspend fun deleteChatById(chat: ChatDto): ChatDto {
        try {
            firestore.collection(CHATS_COLLECTION)
                .document(chat.id).delete()
                .await()
            return chat;
        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e , e.stackTrace);
        }
    }

    override suspend fun deleteMultipleChats(chatList: List<ChatDto>): List<ChatDto> {
        try {
            val batch = firestore.batch()
            chatList.forEach { chat ->
                val docRef = firestore.collection(CHATS_COLLECTION).document(chat.id)
                batch.delete(docRef)
            }

            batch.commit().await()

            return  chatList

        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e , e.stackTrace);
        }
    }

    override suspend fun getChatById(chat: ChatDto): ChatDto {
        try {
            val data = firestore.collection(CHATS_COLLECTION).document(chat.id).get().await()
            return data.toObject(ChatDto::class.java) ?: ChatDto()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e, e.stackTrace)
        }
    }

    override suspend fun getAllUserChatList(userId: String): Flow<List<ChatDto>> {
        try {
            val senderFlow = firestore.collection(CHATS_COLLECTION)
                .whereArrayContains("participantsIds", userId)
                .snapshots()
                .map { snapshot -> snapshot.toObjects(ChatDto::class.java) }


            return senderFlow

        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e, e.stackTrace)
        }
    }


    override suspend fun createMessage(message: MessageDto): MessageDto {
        val update = mutableMapOf<String, Any?>(
            "lastMessage" to message?.text,
            "lastMessageTime" to message.timestamp,
            "type" to message.type,
        )

        val querySnapshot = firestore.collection(CHATS_COLLECTION)
            .whereEqualTo("id", message.chatId)
            .get()
            .await()

        firestore.collection(MESSAGE_COLLECTION)
            .document()
            .set(message)
            .await()


        val existingDoc = querySnapshot.documents.firstOrNull()


        if (existingDoc != null) {
            val updatedChat = existingDoc.toObject(ChatDto::class.java)

            updatedChat?.let { chat ->
                val updatedParticipants = chat.participants.map { participant ->
                    if (participant.id != message.senderId) {
                        participant.copy(readed = 0)
                    } else {
                        participant
                    }
                }

                update.put("participants",updatedParticipants)

                firestore.collection(CHATS_COLLECTION)
                    .document(existingDoc.id)
                    .update(update)
                    .await()
            }
        }

        return message;
    }

    override suspend fun getAllMessageList(chat: ChatDto): Flow<List<MessageDto>> {
        try {
            val data =   firestore.collection(MESSAGE_COLLECTION)
                .whereEqualTo("chatId", chat.id)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .snapshots().map { snapshot ->
                    snapshot.toObjects<MessageDto>(MessageDto::class.java)
                }
            return  data

        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e , e.stackTrace);
        }

    }

    override suspend fun deleteMultipleMessage(messageList: List<MessageDto>): List<MessageDto> {
        try {
            val batch = firestore.batch()
            messageList.forEach { chat ->
                val docRef = firestore.collection(MESSAGE_COLLECTION).document(chat.id)
                batch.delete(docRef)
            }

            batch.commit().await()

            return  messageList

        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e , e.stackTrace);
        }
    }


}