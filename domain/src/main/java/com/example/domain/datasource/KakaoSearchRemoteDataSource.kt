package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.PlaceSearchResponseDto
import kotlinx.coroutines.flow.Flow

interface KakaoSearchRemoteDataSource {

    suspend fun searchKakao(
        query: String
    ): Flow<ApiResult<PlaceSearchResponseDto>>
}