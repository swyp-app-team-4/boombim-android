package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.CheckUserPlaceResponse
import com.example.domain.model.MakeAutoMessageResponse
import com.example.domain.model.MakeCongestionResponse
import com.example.domain.model.PlaceBoomBimResponse
import com.example.domain.model.PlaceLessBoomBimResponse
import com.example.domain.model.RegionResponse
import kotlinx.coroutines.flow.Flow

interface HomeRemoteDataSource {

    suspend fun getBoombimList(): Flow<ApiResult<PlaceBoomBimResponse>>

    suspend fun getRegionList(
        date: String
    ): Flow<ApiResult<List<RegionResponse>>>

    suspend fun makeCongestion(
        memberPlaceId: Int,
        congestionLevelId: Int,
        congestionMessage: String,
        latitude: Double,
        longitude: Double
    ): Flow<ApiResult<MakeCongestionResponse>>

    suspend fun getLessBoomBimList(
        latitude: Double,
        longitude: Double
    ): Flow<ApiResult<PlaceLessBoomBimResponse>>

    suspend fun makeAutoMessage(
        memberPlaceName: String,
        congestionLevelName: String,
        congestionMessage: String
    ): Flow<ApiResult<MakeAutoMessageResponse>>
}