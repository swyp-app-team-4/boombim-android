package com.example.domain.repository

import com.example.domain.model.CongestionData
import kotlinx.coroutines.flow.Flow

interface MapRepository {

    fun getViewPortList(): Flow<List<CongestionData>>

    suspend fun postViewPort(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double
    )
}