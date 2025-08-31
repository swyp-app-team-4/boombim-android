package com.example.data.network.home.request

import kotlinx.serialization.Serializable

@Serializable
data class MakeCongestionRequest (
    val memberPlaceId: Int,
    val congestionLevelId: Int,
    val congestionMessage: String,
    val latitude: Double,
    val longitude: Double
)