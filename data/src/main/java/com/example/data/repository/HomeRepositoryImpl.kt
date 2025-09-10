package com.example.data.repository

import com.example.data.extension.covertApiResultToActionResultIfSuccess
import com.example.data.network.home.HomeApi
import com.example.data.network.home.request.CheckUserPlaceRequest
import com.example.data.network.safeFlow
import com.example.domain.datasource.HomeRemoteDataSource
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.CheckUserPlaceResponse
import com.example.domain.model.MakeAutoMessageResponse
import com.example.domain.model.MakeCongestionResponse
import com.example.domain.model.NotificationModel
import com.example.domain.model.PlaceBoomBimModel
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.domain.model.RegionResponse
import com.example.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource,
    private val homeApi: HomeApi
) : HomeRepository {

    // 지역 소식을 저장하는 StateFlow
    private val _regionList = MutableStateFlow(emptyList<RegionResponse>())

    private val regionList get() = _regionList.asStateFlow()

    // 붐빔 지역을 저장하는 StateFlow
    private val _boomBimList = MutableStateFlow(emptyList<PlaceBoomBimModel>())

    private val boomBimList get() = _boomBimList.asStateFlow()

    // 덜 붐빔 지역을 저장하는 StateFlow
    private val _lessBoomBimList = MutableStateFlow(emptyList<PlaceLessBoomBimModel>())

    private val lessBoomBimList get() = _lessBoomBimList.asStateFlow()



    override fun getRegionDataList(): Flow<List<RegionResponse>> = regionList

    override fun getTop5BoomBimPlaceList(): Flow<List<PlaceBoomBimModel>> = boomBimList

    override fun getLessBoomBimPlaceList(): Flow<List<PlaceLessBoomBimModel>> = lessBoomBimList

    override suspend fun getRegionData(date: String) {
       homeRemoteDataSource.getRegionList(date).first().let { result ->
           if(result is ApiResult.Success){
               _regionList.update {
                   result.data
               }
           }
       }
    }

    override suspend fun getTop5BoomBimData() {
       homeRemoteDataSource.getBoombimList().first().let { result ->
           if (result is ApiResult.Success){
               _boomBimList.update {
                   result.data.data
               }
           }
       }
    }

    override suspend fun getLessBoomBimData(latitude: Double, longitude: Double) {
        homeRemoteDataSource.getLessBoomBimList(latitude, longitude).first().let { result ->
            if (result is ApiResult.Success){
                _lessBoomBimList.update {
                    result.data.data
                }
            }
        }
    }

    override suspend fun checkUserPlace(
        uuid: String,
        name: String,
        address: String,
        latitude: Double,
        longitude: Double
    ): Flow<ApiResult<CheckUserPlaceResponse>> {
        return safeFlow {
            val request = CheckUserPlaceRequest(uuid, name,address, latitude, longitude)
            val result = homeApi.checkUserPlace(request)

            result
        }
    }

    override suspend fun makeCongestion(
        memberPlaceId: Int,
        congestionLevelId: Int,
        congestionMessage: String,
        latitude: Double,
        longitude: Double
    ): ActionResult<MakeCongestionResponse> {
        val result = homeRemoteDataSource.makeCongestion(
            memberPlaceId,
            congestionLevelId,
            congestionMessage,
            latitude,
            longitude
        ).first()
        return result.covertApiResultToActionResultIfSuccess()
    }

    override suspend fun makeAutoMessage(
        memberPlaceName: String,
        congestionLevelName: String,
        congestionMessage: String
    ): Flow<ApiResult<MakeAutoMessageResponse>> {
        val result = homeRemoteDataSource.makeAutoMessage(memberPlaceName, congestionLevelName, congestionMessage)

        return result
    }
}