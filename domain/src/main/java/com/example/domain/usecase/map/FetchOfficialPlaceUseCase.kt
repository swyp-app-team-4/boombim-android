package com.example.domain.usecase.map

import com.example.domain.repository.MapRepository
import javax.inject.Inject

class FetchOfficialPlaceUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend operator fun invoke(placeId: Int) {
        mapRepository.getOfficialPlaceOverview(placeId)
    }
}