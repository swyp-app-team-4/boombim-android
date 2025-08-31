package com.example.domain.usecase.home

import com.example.domain.model.ActionResult
import com.example.domain.model.MakeCongestionResponse
import com.example.domain.repository.HomeRepository
import javax.inject.Inject

class MakeCongestionUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(
        memberPlaceId: Int,
        congestionLevelId: Int,
        congestionMessage: String,
        latitude: Double,
        longitude: Double
    ) : ActionResult<MakeCongestionResponse> {
        return homeRepository.makeCongestion(
            memberPlaceId,
            congestionLevelId,
            congestionMessage,
            latitude,
            longitude
        )
    }
}