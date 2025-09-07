package com.example.domain.usecase.home

import com.example.domain.repository.HomeRepository
import javax.inject.Inject

class GetBoomBimUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke() = homeRepository.getTop5BoomBimPlaceList()
}