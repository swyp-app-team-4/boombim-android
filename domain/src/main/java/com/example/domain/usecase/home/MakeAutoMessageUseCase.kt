package com.example.domain.usecase.home

import com.example.domain.model.ApiResult
import com.example.domain.model.MakeAutoMessageResponse
import com.example.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MakeAutoMessageUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(memberPlaceName: String, congestionLevelName: String, congestionMessage: String): Flow<ApiResult<MakeAutoMessageResponse>> {
        return homeRepository.makeAutoMessage(memberPlaceName, congestionLevelName, congestionMessage)
    }
}