package com.example.domain.usecase.home

import com.example.domain.model.AiTokenResponse
import com.example.domain.model.ApiResult
import com.example.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClovaTokenUseCase @Inject constructor(
    private val homeRepository: HomeRepository
){
    suspend operator fun invoke(memberPlaceId: Int): Flow<ApiResult<AiTokenResponse>>{
        return homeRepository.getClovaToken(memberPlaceId)
    }
}