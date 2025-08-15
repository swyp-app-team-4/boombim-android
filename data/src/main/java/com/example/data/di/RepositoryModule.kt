package com.example.data.di

import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.NotificationRepositoryImpl
import com.example.data.repository.VoteRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.NotificationRepository
import com.example.domain.repository.VoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindVoteRepository(
        impl: VoteRepositoryImpl
    ): VoteRepository
}