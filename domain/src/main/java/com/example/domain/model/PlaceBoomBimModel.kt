package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaceBoomBimResponse (
    val code: Int,
    val status: String,
    val message: String,
    val data: List<PlaceBoomBimModel>
)

@Serializable
data class PlaceBoomBimModel(
    val officialPlaceId: Int,
    val officialPlaceName: String,
    val legalDong: String,
    val imageUrl: String,
    val congestionLevelName: String,
    val densityPerM2: Double,
    val observedAt: String
)