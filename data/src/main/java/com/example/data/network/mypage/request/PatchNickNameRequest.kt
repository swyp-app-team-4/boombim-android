package com.example.data.network.mypage.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatchNickNameRequest (
    val name: String
)