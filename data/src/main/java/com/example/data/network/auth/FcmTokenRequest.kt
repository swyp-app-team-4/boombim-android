package com.example.data.network.auth

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest(
    val token: String,
    val deviceType: String
)