package com.example.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class AiTokenResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: AiTokenData
)

@Serializable
data class AiTokenData(
    val aiAttemptToken: String
)