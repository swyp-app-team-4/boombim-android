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
    val officialPlaceId: Long,
    val officialPlaceName: String,
    val placeType: String,
    val poiCode: String,
    val observedAt: String,
    val centroidLatitude: Double,
    val centroidLongitude: Double,
    val polygonCoordinates: String,
    val demographics: List<Demographic>,
    val forecasts: List<Forecast>,
    val imageUrl: String,
    val isFavorite: Boolean,
    val legalDong: String,
    val congestionLevelName: String,
    val congestionMessage: String,
    val minimumPopulation: Int,
    val maximumPopulation: Int
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
