package com.example.data.datasource

import com.example.data.network.home.HomeApi
import com.example.data.network.safeFlow
import com.example.domain.datasource.HomeRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.RegionResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeRemoteDataSource{

    override suspend fun getRegionList(date: String): Flow<ApiResult<List<RegionResponse>>> {
        return safeFlow {
            homeApi.getRegionList(date)
        }
    }
}