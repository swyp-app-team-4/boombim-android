package com.example.domain.usecase.kakaosearch

import com.example.domain.repository.KakaoSearchRepository
import javax.inject.Inject

class GetKakaoSearchUseCase @Inject constructor(
    private val kakaoSearchRepository: KakaoSearchRepository
){
    operator fun invoke() = kakaoSearchRepository.getKakaoSearchList()
}