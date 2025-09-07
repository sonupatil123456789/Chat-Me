package com.example.mechat

import ChatModel
import ParticipantModel
import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.smartjobreminder.core.resources.MessageType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

@Serializable
data class ParticipantNavModel(
    val id: String,
    val name: String,
    val readed: Int
)

@Serializable
data class ChatNavModel(
    val id: String,
    val participantsIds: List<String> = emptyList(),
    val canView: List<String> = emptyList(),
    val participants: List<ParticipantNavModel>,
    val lastMessage: String? = null,
    val unreadCount: Int = 0,
    val type: String,
    val lastMessageTime: Long?,
    val createdAt: Long?
) {
    companion object {
        // Convert from ChatModel -> ChatNavModel
        fun toNavigationModel(chatModel: ChatModel): ChatNavModel {
            return ChatNavModel(
                id = chatModel.id,
                participants = chatModel.participants.map {
                    ParticipantNavModel(it.id,it.name,it.readed)
                },
                participantsIds = chatModel.participantsIds,
                lastMessage =chatModel.lastMessage,
                lastMessageTime = chatModel.lastMessageTime?.time,
                createdAt = chatModel.createdAt?.time,
                unreadCount = chatModel.unreadCount,
                type = chatModel.type.name,

            )
        }

        // Convert from ChatNavModel -> ChatModel
        fun fromNavigationModel(chatNavModel: ChatNavModel): ChatModel {
            return ChatModel(
                id = chatNavModel.id,
                participants = chatNavModel.participants.map {
                    ParticipantModel(it.id,it.name,it.readed)
                },
                participantsIds = chatNavModel.participantsIds,
                lastMessage = chatNavModel.lastMessage,
                lastMessageTime = chatNavModel.lastMessageTime?.let { Date(it) },
                createdAt = chatNavModel.createdAt?.let { Date(it) },
                unreadCount = chatNavModel.unreadCount,
                type = MessageType.valueOf(chatNavModel.type)
            )
        }
    }
}





object CustomNavType {
    val ChatNavType = object : NavType<ChatNavModel>(isNullableAllowed = false) {
        override fun put(bundle: Bundle, key: String, value: ChatNavModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun get(bundle: Bundle, key: String): ChatNavModel? {
            return bundle.getString(key)?.let { jsonString ->
                Json.decodeFromString<ChatNavModel>(jsonString)
            }
        }

        override fun parseValue(value: String): ChatNavModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ChatNavModel): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }
}
