package com.example.data.network.home.request

import kotlinx.serialization.Serializable

@Serializable
data class GetClovaTokenRequest (
    val memberPlaceId: Int
)