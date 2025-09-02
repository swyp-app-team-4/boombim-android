package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VoteErrorResponse (
    val status: Int,
    val code: Int,
    val message: String,
    val time: String
)