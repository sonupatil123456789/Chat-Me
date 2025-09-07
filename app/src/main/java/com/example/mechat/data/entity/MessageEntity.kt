package com.example.mechat.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val chatId: String,
    val senderId: String = "",
    val receiverId : MutableList<String> = mutableListOf(),
    val text: String,
    val timestamp: Long,
    val type: String, // MessageType enum as string
    val isRead: Boolean = false,
    val isDelivered: Boolean = false,
    val imageUrl: String? = null,
    val fileUrl: String? = null,
    val fileName: String? = null,
    val fileSize: Long? = null
)