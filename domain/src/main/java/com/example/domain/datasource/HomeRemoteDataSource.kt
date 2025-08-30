package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.RegionResponse
import kotlinx.coroutines.flow.Flow

interface HomeRemoteDataSource {

    suspend fun getRegionList(
        date: String
    ): Flow<ApiResult<List<RegionResponse>>>
}