package com.example.data.datasource

import com.example.data.network.home.HomeApi
import com.example.data.network.home.request.CheckUserPlaceRequest
import com.example.data.network.home.request.MakeAutoCongestionMessageRequest
import com.example.data.network.home.request.MakeCongestionRequest
import com.example.data.network.safeFlow
import com.example.domain.datasource.HomeRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.CheckUserPlaceResponse
import com.example.domain.model.Coordinate
import com.example.domain.model.MakeAutoMessageResponse
import com.example.domain.model.MakeCongestionResponse
import com.example.domain.model.PlaceBoomBimResponse
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.domain.model.PlaceLessBoomBimResponse
import com.example.domain.model.RegionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeRemoteDataSource{

    override suspend fun getRegionList(date: String): Flow<ApiResult<List<RegionResponse>>> {
        return safeFlow {
            homeApi.getRegionList(date)
        }
    }

    override suspend fun makeCongestion(
        memberPlaceId: Int,
        congestionLevelId: Int,
        congestionMessage: String,
        latitude: Double,
        longitude: Double
    ): Flow<ApiResult<MakeCongestionResponse>> {
        val request = MakeCongestionRequest(
            memberPlaceId = memberPlaceId,
            congestionLevelId = congestionLevelId,
            congestionMessage = congestionMessage,
            latitude = latitude,
            longitude = longitude
        )
        return safeFlow {
            homeApi.postCongestion(request)
        }
    }

    override suspend fun getBoombimList(): Flow<ApiResult<PlaceBoomBimResponse>> {
        return safeFlow {
            homeApi.getTop5BoombimPlace()
        }
    }

    override suspend fun getLessBoomBimList(
        latitude: Double,
        longitude: Double
    ): Flow<ApiResult<PlaceLessBoomBimResponse>> {
        return safeFlow {
            homeApi.getLessBoomBimPlace(latitude,longitude)
        }
    }

    override suspend fun makeAutoMessage(
        memberPlaceName: String,
        congestionLevelName: String,
        congestionMessage: String
    ): Flow<ApiResult<MakeAutoMessageResponse>> {
        val request = MakeAutoCongestionMessageRequest(memberPlaceName, congestionLevelName, congestionMessage)
        return safeFlow {
            homeApi.makeAutoCongestionMessage(request)
        }
    }

}