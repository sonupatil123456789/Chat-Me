package com.example.mechat.di

import com.example.mechat.data.datasource.remote.AuthDataSource
import com.example.mechat.data.datasource.remote.AuthFirebaseDataSourceImpl
import com.example.mechat.data.datasource.remote.ChatDataSource
import com.example.mechat.data.datasource.remote.ChatFirebaseDataSourceImpl
import com.example.mechat.data.repositoryimpl.AuthRepositoryImpl
import com.example.mechat.data.repositoryimpl.ChatRepositoryImpl
import com.example.mechat.domain.repository.AuthRepository
import com.example.mechat.domain.repository.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    abstract fun bindsChatDataSource(chatImpl : ChatFirebaseDataSourceImpl) : ChatDataSource

    @Binds
    abstract fun bindsChatRepository(chatRepo : ChatRepositoryImpl): ChatRepository

}