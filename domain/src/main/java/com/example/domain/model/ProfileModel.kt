package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileModel(
    val name: String = "",
    val profile: String = "",
    val email: String = "",
    val socialProvider: String = "",
    val point: Int = 0
)
