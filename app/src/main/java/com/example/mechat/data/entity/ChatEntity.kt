package com.example.mechat.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,
    val participants: List<ParticipantEntity>, // Use @TypeConverter for this
    val participantsIds: List<String>,
    val lastMessage: String?,
    val lastMessageTime: Long?,
    val createdAt: Long,
    val type: String,
    val unreadCount: Int = 0
)



data class ParticipantEntity(


    val id: String,
    val name: String,
    val readed: Int
)





