package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SocialLoginSignUpResult (
    val accessToken: String,
    val refreshToken: String,
    val nameFlag: Boolean
)