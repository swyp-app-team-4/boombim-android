package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaceLessBoomBimResponse (
    val code: Int,
    val status: String,
    val message: String,
    val data: List<PlaceLessBoomBimModel>
)

@Serializable
data class PlaceLessBoomBimModel(
    val officialPlaceId: Int,
    val officialPlaceName: String,
    val legalDong: String,
    val imageUrl: String,
    val congestionLevelName: String,
    val observedAt: String,
    val distanceMeters: Double
)