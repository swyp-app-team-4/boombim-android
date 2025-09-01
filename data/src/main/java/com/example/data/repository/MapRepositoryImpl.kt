package com.example.data.repository

import com.example.data.network.safeFlow
import com.example.domain.datasource.MapRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.CongestionData
import com.example.domain.model.CongestionResponse
import com.example.domain.model.MemberPlaceData
import com.example.domain.model.MemberPlaceResponse
import com.example.domain.model.PlaceData
import com.example.domain.model.PlaceDocumentDto
import com.example.domain.model.ProfileModel
import com.example.domain.repository.MapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val mapRemoteDataSource: MapRemoteDataSource

) : MapRepository{

    private val _placeOverview = MutableStateFlow<PlaceData?>(null)

    private val placeOverview
        get() = _placeOverview.asStateFlow()

    //공식장소
    private val _mapList = MutableStateFlow(emptyList<CongestionData>())

    private val mapList
        get() = _mapList.asStateFlow()

    //사용자 장소
    private val _memberPlaceList = MutableStateFlow(emptyList<MemberPlaceData>())

    private val memberPlaceList
        get() = _memberPlaceList.asStateFlow()


    override fun getViewPortList(): Flow<List<CongestionData>> = mapList

    override fun getMemberPlaceList(): Flow<List<MemberPlaceData>> = memberPlaceList

    override fun getPlaceOverview(): Flow<PlaceData?> = placeOverview

    override suspend fun postViewPort(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double,
        zoomLevel: Int
    ) {
        mapRemoteDataSource.postViewPort(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            memberLongitude,
            memberLatitude,
            zoomLevel
        ).first().let { result ->
            if (result is ApiResult.Success) {
                _mapList.update {
                    result.data.data
                }
            }
        }
    }

    override suspend fun postMemberPlace(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double,
        zoomLevel: Int
    ) {
        mapRemoteDataSource.postMemberPlace(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            memberLongitude,
            memberLatitude,
            zoomLevel
        ).first().let { result ->
            if (result is ApiResult.Success) {
                _memberPlaceList.update {
                    result.data.data
                }
            }
        }
    }

    override suspend fun getOfficialPlaceOverview(officialPlaceId: Int) {
        mapRemoteDataSource.getOfficialPlaceOverview(officialPlaceId).first().let { result ->
            if (result is ApiResult.Success) {
                _placeOverview.update {
                    result.data.data
                }
            }
        }
    }


}