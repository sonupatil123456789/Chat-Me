package com.example.mechat.data.dto

import com.example.mechat.data.entity.MessageEntity
import com.example.mechat.domain.model.MessageModel
import com.example.smartjobreminder.core.resources.MessageType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class MessageDto(
    @PropertyName("id") val id: String = "",
    @PropertyName("chatId") val chatId: String = "",
    @PropertyName("senderId")val senderId: String = "",
    @PropertyName("receiverId")val receiverId : MutableList<String> = mutableListOf(),
    @PropertyName("text") val text: String = "",
    @PropertyName("timestamp") val timestamp: Timestamp = Timestamp.now(),
    @PropertyName("type") val type: String = "TEXT",
    @get:PropertyName("isDelivered") @set:PropertyName("isDelivered")
    var isDelivered: Boolean = false,
    @get:PropertyName("isRead") @set:PropertyName("isRead")
    var isRead: Boolean = false,
    @PropertyName("imageUrl") val imageUrl: String? = null,
    @PropertyName("fileUrl") val fileUrl: String? = null,
    @PropertyName("fileName") val fileName: String? = null,
    @PropertyName("fileSize") val fileSize: Long? = null
) {
    fun toDomain(): MessageModel {
        return MessageModel(
            id = id,
            chatId = chatId,
            senderId = senderId,
            receiverId = receiverId,
            text = text,
            timestamp = timestamp.toDate(),
            type = MessageType.valueOf(type),
            isRead = isRead,
            isDelivered = isDelivered,
            imageUrl = imageUrl,
            fileUrl = fileUrl,
            fileName = fileName,
            fileSize = fileSize
        )
    }


}
