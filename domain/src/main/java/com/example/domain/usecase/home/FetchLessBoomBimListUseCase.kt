package com.example.domain.usecase.home

import com.example.domain.repository.HomeRepository
import javax.inject.Inject

class FetchLessBoomBimListUseCase @Inject constructor(
    private val homeRepository: HomeRepository
){
    suspend operator fun invoke(latitude: Double, longitude: Double) {
        homeRepository.getLessBoomBimData(latitude, longitude)
    }
}