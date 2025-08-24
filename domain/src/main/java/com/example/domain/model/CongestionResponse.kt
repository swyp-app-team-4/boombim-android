package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CongestionResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: List<CongestionData>
)

@Serializable
data class CongestionData(
    val id: Int,
    val name: String,
    val coordinate: Coordinate,
    val distance: Double,
    val congestionLevelName: String,
    val congestionMessage: String
)

@Serializable
data class Coordinate(
    val latitude: Double,
    val longitude: Double
)
