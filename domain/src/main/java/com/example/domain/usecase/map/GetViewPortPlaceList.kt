package com.example.domain.usecase.map

import com.example.domain.repository.MapRepository
import javax.inject.Inject

class GetViewPortPlaceList @Inject constructor(
    private val mapRepository: MapRepository
) {
    operator fun invoke() = mapRepository.getViewPortList()
}