package com.example.domain.usecase.home

import com.example.domain.repository.HomeRepository
import javax.inject.Inject

class FetchBoomBimListUseCase @Inject constructor(
    private val homeRepository: HomeRepository
){
    suspend operator fun invoke() {
        homeRepository.getTop5BoomBimData()
    }
}