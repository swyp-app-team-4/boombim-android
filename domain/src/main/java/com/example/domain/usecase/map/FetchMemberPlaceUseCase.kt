package com.example.domain.usecase.map

import com.example.domain.repository.MapRepository
import javax.inject.Inject

class FetchMemberPlaceUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend operator fun invoke(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double,
        zoomLevel: Int
    ){
        mapRepository.postMemberPlace(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            memberLongitude,
            memberLatitude
            ,zoomLevel
        )
    }
}