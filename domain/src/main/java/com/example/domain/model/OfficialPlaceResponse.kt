package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OfficialPlaceResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: PlaceData
)

@Serializable
data class PlaceData(
    val id: Int,
    val name: String,
    val poiCode: String,
    val centroidLatitude: Double,
    val centroidLongitude: Double,
    val polygonCoordinates: String,
    val demographics: List<Demographic>,
    val forecasts: List<Forecast>
)

@Serializable
data class Demographic(
    val category: String,
    val subCategory: String,
    val rate: Double
)

@Serializable
data class Forecast(
    val forecastTime: String,
    val congestionLevelName: String,
    val forecastPopulationMin: Int,
    val forecastPopulationMax: Int
)
