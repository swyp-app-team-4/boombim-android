package com.example.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class MyActivityResponse (
    val id: String = UUID.randomUUID().toString(),
    val posName: String,
    val congestionLevel: String,
    val createdAt: String
)