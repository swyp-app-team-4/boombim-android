package com.example.domain.usecase.kakaosearch

import com.example.domain.repository.KakaoSearchRepository
import javax.inject.Inject

class FetchKakaoSearchUseCase @Inject constructor(
    private val kakaoSearchRepository: KakaoSearchRepository
) {
    suspend operator fun invoke(query: String) {
        kakaoSearchRepository.searchKakao(query)
    }
}