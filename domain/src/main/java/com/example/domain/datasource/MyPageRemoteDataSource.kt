package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface MyPageRemoteDataSource {

    suspend fun getProfile(): Flow<ApiResult<ProfileModel>>

    suspend fun getMyAnswer(): Flow<ApiResult<List<MyPageVoteResponse>>>

    suspend fun getMyQuestion(): Flow<ApiResult<List<MyPageVoteResponse>>>
}