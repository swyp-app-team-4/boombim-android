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
    val placeId: Int,
    val placeType: String,
    val name: String,
    val imageUrl: String? = null,
    val congestionLevelName: String? = null,
    val observedAt: String? = null,
    val todayUpdateCount: Int? = null,
    val updatedToday: Boolean? = null
)



