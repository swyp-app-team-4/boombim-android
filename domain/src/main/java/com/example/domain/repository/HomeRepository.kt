package com.example.domain.repository

import com.example.domain.model.RegionResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getRegionDataList(): Flow<List<RegionResponse>>

    suspend fun getRegionData(date: String)
}