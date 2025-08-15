package com.example.data.di

import com.example.data.datasource.AuthRemoteDataSourceImpl
import com.example.data.datasource.NotificationRemoteDataSourceImpl
import com.example.data.datasource.VoteRemoteDataSourceImpl
import com.example.domain.datasource.AuthRemoteDataSource
import com.example.domain.datasource.NotificationRemoteDataSource
import com.example.domain.datasource.VoteRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAuthRemoteSource(
        userRemoteDataSource: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindNotificationRemoteSource(
        userRemoteDataSource: NotificationRemoteDataSourceImpl
    ): NotificationRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindVoteRemoteSource(
        userRemoteDataSource: VoteRemoteDataSourceImpl
    ): VoteRemoteDataSource

}