package com.example.data.network.mypage.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountRequest (
    val leaveReason: String
)