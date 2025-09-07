package com.example.mechat.di


import android.content.Context
import androidx.room.Room
import com.example.mechat.core.ChatDatabase
import com.example.mechat.data.datasource.local.ChatDao
import com.example.mechat.data.datasource.local.MessageDao
import com.example.mechat.data.datasource.remote.AuthDataSource
import com.example.mechat.data.datasource.remote.AuthFirebaseDataSourceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GlobalModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseFirestore {
       return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun dbInstance(@ApplicationContext context : Context): ChatDatabase {
        return Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            "chat_database"
        ).build()
    }

    @Provides
    fun provideChatDao(db : ChatDatabase): ChatDao {
        return db.chatDao()
    }


    @Provides
    fun provideMessageDao(db : ChatDatabase): MessageDao{
        return db.messageDao()
    }

}