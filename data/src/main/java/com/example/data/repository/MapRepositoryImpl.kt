package com.example.data.repository

import com.example.data.network.safeFlow
import com.example.domain.datasource.MapRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.CongestionData
import com.example.domain.model.CongestionResponse
import com.example.domain.model.PlaceDocumentDto
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

    private val _mapList = MutableStateFlow(emptyList<CongestionData>())

    private val mapList
        get() = _mapList.asStateFlow()


    override fun getViewPortList(): Flow<List<CongestionData>> = mapList

    override suspend fun postViewPort(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double
    ) {
        mapRemoteDataSource.postViewPort(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            memberLongitude,
            memberLatitude
        ).first().let { result ->
            if (result is ApiResult.Success) {
                _mapList.update {
                    result.data.data
                }
            }
        }
    }


}