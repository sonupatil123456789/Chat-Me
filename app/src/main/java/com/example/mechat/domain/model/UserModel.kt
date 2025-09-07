package com.example.mechat.domain.model

import com.example.mechat.data.dto.UserDto
import com.example.smartjobreminder.core.resources.UserStatus
import java.util.Date
import java.util.UUID

data class UserModel(
    val id: String = UUID.randomUUID().toString(),
    val userName: String = "",
    val email: String,
    val profileImageUrl: String? = null,
    val status: UserStatus,
    val isOnline: Boolean,
    val lastSeen: Date = Date(),
    val createdAt: Date,
    val password : String,
) {
    fun toFirebaseModel(): UserDto {
        return UserDto(
            id = id,
            userName = userName.lowercase(),
            email = email,
            profileImageUrl = profileImageUrl,
            status = status.name,
            isOnline = isOnline,
            lastSeen = com.google.firebase.Timestamp(lastSeen),
            createdAt = com.google.firebase.Timestamp(createdAt),
            password =  password
        )
    }
}
