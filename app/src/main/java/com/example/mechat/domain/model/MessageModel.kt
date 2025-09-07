package com.example.mechat.domain.model

import com.example.mechat.data.dto.MessageDto
import com.example.mechat.data.entity.MessageEntity
import com.example.smartjobreminder.core.resources.MessageSide
import com.example.smartjobreminder.core.resources.MessageType
import java.util.Date
import java.util.UUID

data class MessageModel(
    val id: String =  UUID.randomUUID().toString(),
    val chatId: String,
    val senderId: String = "",
    val receiverId : MutableList<String> = mutableListOf(),
    val text: String,
    val timestamp: Date,
    val type: MessageType,
    val isRead: Boolean = false,
    val messageSide : MessageSide = MessageSide.RIGHT,
    val isDelivered: Boolean = false,
    val imageUrl: String? = null,
    val fileUrl: String? = null,
    val fileName: String? = null,
    val fileSize: Long? = null
){


    fun toFirebaseModel(): MessageDto {
        return MessageDto(
            id = id,
            chatId = chatId,
            senderId = senderId,
            receiverId = receiverId,
            text = text,
            timestamp = com.google.firebase.Timestamp(timestamp),
            type = type.name,
            isRead = isRead,
            isDelivered = isDelivered,
            imageUrl = imageUrl,
            fileUrl = fileUrl,
            fileName = fileName,
            fileSize = fileSize
        )
    }
}