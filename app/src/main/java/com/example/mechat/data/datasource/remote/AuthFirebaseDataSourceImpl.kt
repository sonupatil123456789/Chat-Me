package com.example.mechat.data.datasource.remote


import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

// Firebase data source implementation for authantication

val TAG = "AuthFirebaseDataSourceImpl";

@Singleton
class AuthFirebaseDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthDataSource {

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    override fun isUserLoggedIn(): FirebaseUser? = firebaseAuth.currentUser

    override suspend fun signIn(email: String, password: String): FirebaseUser {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            return  result.user!!
        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            throw ExceptionHandler.handleFirebaseAuthException(e , e.stackTrace);
        }
    }

    override suspend fun signeUp(email: String, password: String): FirebaseUser {
         try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
             return  result.user!!
        } catch (e: Exception) {
             Log.d(TAG,e.toString())
             throw ExceptionHandler.handleFirebaseAuthException(e , e.stackTrace);
        }
    }

    override fun signOut(): Boolean {
        try {
            firebaseAuth.signOut()
            return true ;

        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            throw ExceptionHandler.handleFirebaseAuthException(e , e.stackTrace);
        }
    }

    override suspend fun deleteAccount(): Boolean {
         try {
            currentUser?.delete()?.await()
            return true ;
        } catch (e: Exception) {
             Log.d(TAG,e.toString())
             throw ExceptionHandler.handleFirebaseAuthException(e , e.stackTrace);
        }
    }


}