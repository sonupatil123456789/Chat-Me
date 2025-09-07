package com.example.mechat.data.datasource.remote



import android.util.Log
import com.example.mechat.data.dto.UserDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
)  : UserDataSource{



    companion object {
        private const val USERS_COLLECTION = "users"
        private val TAG = "UserFirebaseDataSourceImpl";
    }

    override suspend fun createUser(user: UserDto): UserDto {
        try {
            firestore.collection(USERS_COLLECTION)
                .document(user.id)
                .set(user)
                .await()
            return user;
        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e , e.stackTrace);
        }
    }

    override suspend fun deleteUserById(user: UserDto): UserDto {
        try {
            firestore.collection(USERS_COLLECTION)
                .document(user.id).delete()
                .await()
            return user;
        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e , e.stackTrace);
        }
    }

    override suspend fun getUserById(user: UserDto): UserDto {
        try {
            val data =   firestore.collection(USERS_COLLECTION)
                .document(user.id).get()
                .await()
            return data.toObject(UserDto::class.java) ?: UserDto();
        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e , e.stackTrace);
        }
    }

    override suspend fun getAllUsersList(myId : String, search : String ): Flow<List<UserDto>> {
        try {

            val baseQuery = firestore.collection(USERS_COLLECTION)

            val query = if (search.isNotEmpty()) {
                baseQuery
                    .whereGreaterThanOrEqualTo("userName", search.lowercase())
                    .whereLessThanOrEqualTo("userName", search + "\uf8ff")
            } else {
                baseQuery
            }

            return query.snapshots().map { snapshot ->
                snapshot.toObjects(UserDto::class.java)
                    .filter { it.id != myId }
            }


        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            throw ExceptionHandler.handleFirebaseDbException(e , e.stackTrace);
        }
    }





}