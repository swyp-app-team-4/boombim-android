package com.example.domain.usecase.home

import com.example.domain.repository.HomeRepository
import javax.inject.Inject

class FetchRegionUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(date: String) {
        homeRepository.getRegionData(date)
    }
}