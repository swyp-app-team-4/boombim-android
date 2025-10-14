package com.example.data.di

import com.example.data.datasource.KakaoSearchRemoteDataSourceImpl
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.FavoriteRepositoryImpl
import com.example.data.repository.HomeRepositoryImpl
import com.example.data.repository.KakaoSearchRepositoryImpl
import com.example.data.repository.MapRepositoryImpl
import com.example.data.repository.MyPageRepositoryImpl
import com.example.data.repository.NotificationRepositoryImpl
import com.example.domain.datasource.MyPageRemoteDataSource
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.FavoriteRepository
import com.example.domain.repository.HomeRepository
import com.example.domain.repository.KakaoSearchRepository
import com.example.domain.repository.MapRepository
import com.example.domain.repository.MyPageRepository
import com.example.domain.repository.NotificationRepository
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
    abstract fun bindKakaoRepository(
        impl: KakaoSearchRepositoryImpl
    ): KakaoSearchRepository

    @Binds
    @Singleton
    abstract fun bindMyPageRepository(
        impl: MyPageRepositoryImpl
    ): MyPageRepository

    @Binds
    @Singleton
    abstract fun bindMapRepository(
        impl: MapRepositoryImpl
    ): MapRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository
}