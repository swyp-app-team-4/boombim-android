package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.MyActivityResponse
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.PatchProfileImageResponse
import com.example.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface MyPageRemoteDataSource {

    suspend fun getProfile(): Flow<ApiResult<ProfileModel>>

    suspend fun getMyAnswer(): Flow<ApiResult<List<MyPageVoteResponse>>>

    suspend fun getMyQuestion(): Flow<ApiResult<List<MyPageVoteResponse>>>

    suspend fun patchNickName(name: String): ApiResult<*>

    suspend fun logout(refresh: String): ApiResult<*>

    suspend fun patchProfileImage(imagePath: String): ApiResult<PatchProfileImageResponse>

    suspend fun deleteUser(reason: String): ApiResult<*>

    suspend fun getMyActivity(): Flow<ApiResult<List<MyActivityResponse>>>
}