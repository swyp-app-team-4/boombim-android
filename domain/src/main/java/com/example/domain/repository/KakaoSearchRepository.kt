package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.PlaceDocumentDto
import kotlinx.coroutines.flow.Flow

interface KakaoSearchRepository {

    fun getKakaoSearchList(): Flow<List<PlaceDocumentDto>>
    /**
     *  카카오 검색 목록을 불러온다
     */
    fun searchKakao(
        query: String
    ): Flow<PagingData<PlaceDocumentDto>>
}