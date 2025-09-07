package com.example.mechat.data.dto

import com.example.mechat.domain.model.UserModel
import com.example.smartjobreminder.core.resources.UserStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class UserDto(
    @PropertyName("id") val id: String = "",
    @PropertyName("userName") val userName: String = "",
    @PropertyName("password")  val password : String = "",
    @PropertyName("email") val email: String = "",
    @PropertyName("profileImageUrl") val profileImageUrl: String? = null,
    @PropertyName("status") val status: String = "OFFLINE",
    @PropertyName("isOnline") val isOnline: Boolean = false,
    @PropertyName("lastSeen") val lastSeen: Timestamp = Timestamp.now(),
    @PropertyName("createdAt") val createdAt: Timestamp = Timestamp.now()
) {
    fun toDomain(): UserModel {
        return UserModel(
            id = id,
            userName = userName,
            email = email,
            profileImageUrl = profileImageUrl,
            status = UserStatus.valueOf(status),
            isOnline = isOnline,
            lastSeen = lastSeen.toDate(),
            createdAt = createdAt.toDate(),
            password = password
        )
    }
}