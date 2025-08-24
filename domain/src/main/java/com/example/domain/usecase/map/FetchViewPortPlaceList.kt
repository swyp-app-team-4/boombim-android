package com.example.domain.usecase.map

import com.example.domain.repository.MapRepository
import javax.inject.Inject

class FetchViewPortPlaceList @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend operator fun invoke(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double
    ){
        mapRepository.postViewPort(
            topLeftLongitude,
            topLeftLatitude,
            bottomRightLongitude,
            bottomRightLatitude,
            memberLongitude,
            memberLatitude)
    }
}