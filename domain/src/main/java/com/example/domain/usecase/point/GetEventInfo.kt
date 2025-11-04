package com.example.domain.usecase.point

import com.example.domain.repository.MyPageRepository
import javax.inject.Inject

class GetEventInfo @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    operator fun invoke() = myPageRepository.getEventCampaignInfo()
}