package com.example.data.di

import com.example.data.datasource.KakaoSearchRemoteDataSourceImpl
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.KakaoSearchRepositoryImpl
import com.example.data.repository.MyPageRepositoryImpl
import com.example.data.repository.NotificationRepositoryImpl
import com.example.data.repository.VoteRepositoryImpl
import com.example.domain.datasource.MyPageRemoteDataSource
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.KakaoSearchRepository
import com.example.domain.repository.MyPageRepository
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

    @Binds
    @Singleton
    abstract fun bindKakaoRepository(
        impl: KakaoSearchRepositoryImpl
    ): KakaoSearchRepository

    @Binds
    @Singleton
    abstract fun bindMyPageRepository(
        impl: MyPageRepositoryImpl
    ): MyPageRepository
}