package com.example.domain.usecase.map

import com.example.domain.repository.MapRepository
import javax.inject.Inject

class FetchMemberPlaceDetailUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend operator fun invoke(officialPlaceId: Int){
        mapRepository.getMemberPlaceDetail(officialPlaceId)
    }
}