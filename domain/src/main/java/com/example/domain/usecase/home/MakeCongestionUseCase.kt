package com.example.domain.usecase.home

import com.example.domain.model.ActionResult
import com.example.domain.model.MakeCongestionResponse
import com.example.domain.repository.HomeRepository
import com.example.domain.repository.MyPageRepository
import javax.inject.Inject

class MakeCongestionUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        memberPlaceId: Int,
        congestionLevelId: Int,
        congestionMessage: String,
        latitude: Double,
        longitude: Double
    ) : ActionResult<MakeCongestionResponse> {

        val result = homeRepository.makeCongestion(
            memberPlaceId, congestionLevelId, congestionMessage, latitude, longitude
        )

        if (result is ActionResult.Success) {
            myPageRepository.addPoint(10)
        }

        return result
    }
}