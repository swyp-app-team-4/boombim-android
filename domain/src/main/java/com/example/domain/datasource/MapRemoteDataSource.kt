package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.CongestionResponse
import com.example.domain.model.OfficialPlaceResponse
import kotlinx.coroutines.flow.Flow

interface MapRemoteDataSource {

    suspend fun postViewPort(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double
    ): Flow<ApiResult<CongestionResponse>>

    suspend fun getOfficialPlaceOverview(officialPlaceId: Int): Flow<ApiResult<OfficialPlaceResponse>>
}