package com.example.domain.usecase.map

import com.example.domain.repository.MapRepository
import javax.inject.Inject

class GetMemberPlaceDetailUseCase @Inject constructor(
    private val mapRepository: MapRepository
){
    operator fun invoke() = mapRepository.getMemberPlaceDetailList()
}