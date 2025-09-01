package com.example.data.network.map.request

import com.example.domain.model.Coordinate
import kotlinx.serialization.Serializable


@Serializable
data class PostViewPortRequest(
    val topLeft: Coordinate,
    val bottomRight: Coordinate,
    val memberCoordinate: Coordinate,
    val zoomLevel: Int
)

