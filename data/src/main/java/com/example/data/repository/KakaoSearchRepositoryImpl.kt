package com.example.data.repository

import com.example.domain.datasource.KakaoSearchRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.NotificationModel
import com.example.domain.model.PlaceDocumentDto
import com.example.domain.repository.KakaoSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class KakaoSearchRepositoryImpl @Inject constructor(
    private val kakaoSearchRemoteDataSource: KakaoSearchRemoteDataSource
) : KakaoSearchRepository{

    // 검색 목록을 저장하는 StateFlow
    private val _kakaoSearchList = MutableStateFlow(emptyList<PlaceDocumentDto>())

    private val kakaoSearchList
        get() = _kakaoSearchList.asStateFlow()

    override fun getKakaoSearchList(): Flow<List<PlaceDocumentDto>> = kakaoSearchList

    override suspend fun searchKakao(query: String) {
        kakaoSearchRemoteDataSource.searchKakao(query).first().let { result ->
            if (result is ApiResult.Success) {
                _kakaoSearchList.update {
                    result.data.documents
                }
            }
        }
    }
}