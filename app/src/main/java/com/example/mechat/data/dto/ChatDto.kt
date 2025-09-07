package com.example.mechat.data.dto
import ChatModel
import ParticipantModel
import com.example.mechat.data.entity.ChatEntity
import com.example.smartjobreminder.core.resources.MessageType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName


data class ParticipantDto(
    @PropertyName("id") val id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("readed") val readed: Int = 0
) {
    fun toDomain(): ParticipantModel {
        return ParticipantModel(
            id = id,
            name = name,
            readed = readed
        )
    }
}

data class ChatDto(
    @PropertyName("id") val id: String = "",
    @PropertyName("participants") val participants: List<ParticipantDto> = emptyList(),
    @PropertyName("participantsIds") val participantsIds: List<String> = emptyList(),
    @PropertyName("lastMessage") val lastMessage: String? = null,
    @PropertyName("lastMessageTime") val lastMessageTime: Timestamp? = Timestamp.now(),
    @PropertyName("createdAt") val createdAt: Timestamp = Timestamp.now(),
    @PropertyName("type") val type: String = "TEXT",
    @PropertyName("unreadCount") val unreadCount: Int = 0
) {
    fun toDomain(): ChatModel {
        return ChatModel(
            id = id,
            participants = participants.map { it.toDomain() },
            participantsIds = participantsIds ,
            lastMessage = lastMessage,
            lastMessageTime = lastMessageTime?.toDate(),
            createdAt = createdAt.toDate(),
            type = MessageType.valueOf(type),
            unreadCount = unreadCount
        )
    }


}
