package com.example.data.di

import com.example.data.provider.FcmTokenProviderImpl
import com.example.domain.provider.FcmTokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProviderModule {

    @Binds
    @Singleton
    abstract fun bindFcmTokenProvider(
        impl: FcmTokenProviderImpl
    ): FcmTokenProvider
}