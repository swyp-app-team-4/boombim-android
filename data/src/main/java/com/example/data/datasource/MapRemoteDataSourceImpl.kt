package com.example.data.datasource

import com.example.data.network.map.MapApi
import com.example.data.network.map.request.PostViewPortRequest
import com.example.data.network.safeFlow
import com.example.domain.datasource.MapRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.CongestionResponse
import com.example.domain.model.Coordinate
import com.example.domain.model.OfficialPlaceResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MapRemoteDataSourceImpl @Inject constructor(
    private val mapApi: MapApi
) : MapRemoteDataSource{

    override suspend fun postViewPort(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double
    ): Flow<ApiResult<CongestionResponse>> {
        val request = PostViewPortRequest(
            topLeft = Coordinate(topLeftLatitude, topLeftLongitude),
            bottomRight = Coordinate(bottomRightLatitude, bottomRightLongitude),
            memberCoordinate = Coordinate(memberLatitude, memberLongitude)
        )
        return safeFlow { mapApi.postViewPort(request)}
    }

    override suspend fun getOfficialPlaceOverview(officialPlaceId: Int): Flow<ApiResult<OfficialPlaceResponse>> {
        return safeFlow { mapApi.getOfficialPlaceOverview(officialPlaceId) }
    }

}