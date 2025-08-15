package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.VoteResponse
import kotlinx.coroutines.flow.Flow

interface VoteRemoteDataSource {

    suspend fun getVoteList(
        latitude: Double,
        longitude : Double,
    ): Flow<ApiResult<VoteResponse>>
}