package com.example.mechat.di

import com.example.mechat.data.datasource.remote.AuthDataSource
import com.example.mechat.data.datasource.remote.AuthFirebaseDataSourceImpl
import com.example.mechat.data.datasource.remote.UserDataSource
import com.example.mechat.data.datasource.remote.UserFirebaseDataSourceImpl
import com.example.mechat.data.repositoryimpl.AuthRepositoryImpl
import com.example.mechat.domain.repository.AuthRepository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract  fun bindsUserDataSource(userImpl : UserFirebaseDataSourceImpl) : UserDataSource

    @Binds
    abstract fun bindsAuthDataSource(authImpl : AuthFirebaseDataSourceImpl) : AuthDataSource

    @Binds
    abstract fun bindsAuthRepository(authRepo : AuthRepositoryImpl) : AuthRepository




}