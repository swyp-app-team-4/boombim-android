package com.example.domain.usecase.kakaosearch

import androidx.paging.PagingData
import com.example.domain.model.PlaceDocumentDto
import com.example.domain.repository.KakaoSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchKakaoPlaceUseCase @Inject constructor(
    private val kakaoSearchRepository: KakaoSearchRepository
) {

    operator fun invoke(query: String): Flow<PagingData<PlaceDocumentDto>> {
        return kakaoSearchRepository.searchKakao(query)
    }

}