package com.example.data.datasource

import com.example.data.network.kakaosearch.KakaoLocalApi
import com.example.data.network.safeFlow
import com.example.data.network.vote.VoteApi
import com.example.domain.datasource.KakaoSearchRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.PlaceSearchResponseDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KakaoSearchRemoteDataSourceImpl @Inject constructor(
    private val kakaoLocalApi: KakaoLocalApi
) : KakaoSearchRemoteDataSource {

    override suspend fun searchKakao(
        query: String,
        page: Int,
        size: Int
    ): PlaceSearchResponseDto {
        return kakaoLocalApi.searchPlaces(
            query = query,
            page = page,
            size = size
        )
    }

}