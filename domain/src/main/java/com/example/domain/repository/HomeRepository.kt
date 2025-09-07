package com.example.domain.repository

import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.CheckUserPlaceResponse
import com.example.domain.model.MakeAutoMessageResponse
import com.example.domain.model.MakeCongestionResponse
import com.example.domain.model.PlaceBoomBimModel
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.domain.model.RegionResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getRegionDataList(): Flow<List<RegionResponse>>

    fun getTop5BoomBimPlaceList(): Flow<List<PlaceBoomBimModel>>

    fun getLessBoomBimPlaceList(): Flow<List<PlaceLessBoomBimModel>>

    suspend fun getRegionData(date: String)

    suspend fun getTop5BoomBimData()

    suspend fun getLessBoomBimData(
        latitude: Double,
        longitude: Double
    )

    suspend fun checkUserPlace(
        uuid: String,
        name: String,
        address: String,
        latitude: Double,
        longitude: Double
    ): Flow<ApiResult<CheckUserPlaceResponse>>

    suspend fun makeCongestion(
        memberPlaceId: Int,
        congestionLevelId: Int,
        congestionMessage: String,
        latitude: Double,
        longitude: Double
    ): ActionResult<MakeCongestionResponse>

    suspend fun makeAutoMessage(
        memberPlaceName: String,
        congestionLevelName: String,
        congestionMessage: String
    ): Flow<ApiResult<MakeAutoMessageResponse>>
}