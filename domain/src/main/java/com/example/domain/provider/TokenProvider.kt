package com.example.domain.provider

interface TokenProvider {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?

    suspend fun clearAllUserData()
}