package com.example.data.di

import com.example.data.datasource.AuthRemoteDataSourceImpl
import com.example.data.datasource.KakaoSearchRemoteDataSourceImpl
import com.example.data.datasource.NotificationRemoteDataSourceImpl
import com.example.data.datasource.VoteRemoteDataSourceImpl
import com.example.domain.datasource.AuthRemoteDataSource
import com.example.domain.datasource.KakaoSearchRemoteDataSource
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
        authRemoteDataSource: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindNotificationRemoteSource(
        notificationRemoteDataSource: NotificationRemoteDataSourceImpl
    ): NotificationRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindVoteRemoteSource(
        voteRemoteDataSource: VoteRemoteDataSourceImpl
    ): VoteRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindKakaoRemoteSource(
        kakaoRemoteDataSource: KakaoSearchRemoteDataSourceImpl
    ): KakaoSearchRemoteDataSource

}