package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MakeCongestionResponse (
    val code: Int,
    val status: String,
    val message: String,
    val data: MemberCongestionData
)

@Serializable
data class MemberCongestionData(
    val memberCongestionId: Int,
    val memberPlaceName: String
)
