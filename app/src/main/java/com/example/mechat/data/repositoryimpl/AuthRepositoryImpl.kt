package com.example.mechat.data.repositoryimpl

import android.util.Log
import com.example.mechat.core.resources.AppException
import com.example.mechat.core.resources.DataResult
import com.example.mechat.data.datasource.remote.AuthDataSource
import com.example.mechat.data.datasource.remote.TAG
import com.example.mechat.data.datasource.remote.UserDataSource
import com.example.mechat.data.dto.UserDto
import com.example.mechat.domain.model.UserModel
import com.example.mechat.domain.repository.AuthRepository
import com.example.smartjobreminder.core.resources.ErrorCode
import com.google.android.play.integrity.internal.u
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(

    private val authDataSource: AuthDataSource,
    private  val userDataSource: UserDataSource,
    private val firebaseAuth: FirebaseAuth,


): AuthRepository {

    private val  TAG = "AuthRepositoryImpl"

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid


    override suspend fun isUserLoggedIn(): DataResult<UserModel?> {
        return try {
            withContext(Dispatchers.IO) {
                val result = authDataSource.isUserLoggedIn()

                if (result != null) {
                    val response = userDataSource.getUserById(UserDto(id = result.uid))
                    DataResult.Success("Success", response.toDomain())
                } else {
                    throw AppException(
                        "Error", errorCode = ErrorCode.UNAUTHORISED_ERROR, stack = arrayOf(),
                        title =  ErrorCode.UNAUTHORISED_ERROR.title,
                    )
                }
            }
        } catch (e: AppException) {
            Log.d(TAG, e.toString())
            DataResult.Error(e)
        }
    }


    override suspend fun signIn(
        user : UserModel
    ): DataResult<UserModel> {
        try {
            val result = authDataSource.signIn(user.email,user.password)
            if (result.uid.isNotBlank() || result.uid.isNotEmpty()){
               val response =  userDataSource.getUserById(user.toFirebaseModel().copy(id = result.uid))
                return  DataResult.Success<UserModel>( "Success",response.toDomain())
            }else {

                throw AppException(title = ErrorCode.UNKNOWN_ERROR.title ,message = "Error" , errorCode = ErrorCode.UNKNOWN_ERROR, stack = arrayOf())
            }

        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<UserModel>(e)
        }
    }

    override suspend fun signeUp(
        user : UserModel
    ): DataResult<UserModel> {
        try {
            val result = authDataSource.signeUp(user.email,user.password)
            if (result.uid.isNotBlank() || result.uid.isNotEmpty()){
                val response =  userDataSource.createUser(user.toFirebaseModel().copy(id = result.uid))
                return  DataResult.Success<UserModel>( "Success",response.toDomain())
            }else {
                throw AppException(title = ErrorCode.UNKNOWN_ERROR.title ,message = "Error" , errorCode = ErrorCode.UNKNOWN_ERROR, stack = arrayOf())
            }
        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<UserModel>(e)
        }
    }

    override fun signOut(): DataResult<Boolean> {
        try {
            val result = authDataSource.signOut()
            if (result){
                return  DataResult.Success<Boolean>( "Success", true)
            }else {
                throw AppException(title = ErrorCode.UNKNOWN_ERROR.title ,message = "Error" , errorCode = ErrorCode.UNKNOWN_ERROR, stack = arrayOf())
            }
        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<Boolean>(e)
        }
    }

    override suspend fun deleteAccount(): DataResult<Boolean> {
        try {
            val result = authDataSource.deleteAccount()
            if (result){
                val response =  userDataSource.deleteUserById(UserDto(id = firebaseAuth.uid!!))
                return  DataResult.Success<Boolean>( "Success", true)
            }else {
                throw AppException(title = ErrorCode.UNKNOWN_ERROR.title ,message = "Error" , errorCode = ErrorCode.UNKNOWN_ERROR, stack = arrayOf())
            }
        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<Boolean>(e)
        }
    }

    override suspend fun getAllUsersList(search: String): DataResult<Flow<List<UserModel>>> {
        try {
            return  DataResult.Success<Flow<List<UserModel>>>( "Success",
                userDataSource.getAllUsersList(currentUserId ?: "",search).map { it.map { it.toDomain() }
            })

        } catch (e: AppException) {
            Log.d(TAG,e.toString())
            return  DataResult.Error<Flow<List<UserModel>>>(e)
        }

    }

}