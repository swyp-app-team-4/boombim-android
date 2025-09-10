package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MakeAutoMessageResponse (
    val code: Int,
    val status: String,
    val message: String,
    val data: MessageData
)

@Serializable
data class MessageData(
    val generatedCongestionMessage: String
)