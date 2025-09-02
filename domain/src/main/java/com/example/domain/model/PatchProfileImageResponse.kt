package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PatchProfileImageResponse (
    val profile: String
)