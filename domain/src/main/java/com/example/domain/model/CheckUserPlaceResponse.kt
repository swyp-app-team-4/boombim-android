package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckUserPlaceResponse (
    val code: Int,
    val status: String,
    val message: String,
    val data: MemberPlaceId
)

@Serializable
data class MemberPlaceId(
    val memberPlaceId: Int
)