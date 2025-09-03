package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: List<FavoriteData>
)

@Serializable
data class FavoriteData(
    val favoriteId: Int,
    val memberPlaceId: Int,
    val latitude: Double,
    val longitude: Double,
    val memberPlaceName: String,
    val congestionLevelName: String,
    val congestionMessage: String,
    val observedAt: String
)

