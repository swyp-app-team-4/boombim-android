package com.example.domain.provider

interface FcmTokenProvider {

    suspend fun getSavedFcmToken(): String?

    suspend fun setSavedFcmToken(token: String?)
}