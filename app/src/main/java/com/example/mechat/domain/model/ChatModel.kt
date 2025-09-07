import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.example.mechat.data.dto.ChatDto
import com.example.mechat.data.dto.ParticipantDto
import com.example.smartjobreminder.core.resources.MessageType
import java.util.Date
import java.util.UUID
import kotlin.String




data class ParticipantModel(
    val id: String,
    val name: String,
    val readed: Int // 0 = unread, 1 = read
)

data class ChatModel(
    val id: String = UUID.randomUUID().toString(),
    val participants: List<ParticipantModel> = emptyList(),
    val participantsIds: List<String> = emptyList(),
    val lastMessage: String? = null,
    val lastMessageTime: Date? = Date(),
    val createdAt: Date? = Date(),
    val type: MessageType,
    val unreadCount: Int = 0
) {
    fun toFirebaseModel(): ChatDto {
        return ChatDto(
            id = id,
            participantsIds = participantsIds,
            lastMessage = lastMessage,
            participants = participants.map {
                ParticipantDto(id = it.id, name = it.name, readed = it.readed)
            },
            lastMessageTime = com.google.firebase.Timestamp(lastMessageTime!!),
            createdAt = com.google.firebase.Timestamp(createdAt!!),
            type = type.name,
            unreadCount = unreadCount
        )
    }


}











