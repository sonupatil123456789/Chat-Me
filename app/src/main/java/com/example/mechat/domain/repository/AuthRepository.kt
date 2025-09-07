package com.example.mechat.domain.repository

import com.example.mechat.core.resources.DataResult
import com.example.mechat.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend  fun isUserLoggedIn(): DataResult<UserModel?>

    suspend fun signIn(user : UserModel): DataResult<UserModel>

    suspend fun signeUp(user : UserModel): DataResult<UserModel>

    fun signOut(): DataResult<Boolean>

    suspend fun deleteAccount(): DataResult<Boolean>

    suspend fun getAllUsersList(search : String ):DataResult<Flow<List<UserModel>>>

}