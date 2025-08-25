package com.example.domain.repository

import com.example.domain.model.CongestionData
import com.example.domain.model.PlaceData
import kotlinx.coroutines.flow.Flow

interface MapRepository {

    fun getViewPortList(): Flow<List<CongestionData>>

    fun getPlaceOverview(): Flow<PlaceData?>

    suspend fun postViewPort(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double
    )

    suspend fun getOfficialPlaceOverview(officialPlaceId: Int)
}