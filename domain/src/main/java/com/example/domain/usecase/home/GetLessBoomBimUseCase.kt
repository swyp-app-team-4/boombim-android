package com.example.domain.usecase.home

import com.example.domain.repository.HomeRepository
import javax.inject.Inject

class GetLessBoomBimUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke() = homeRepository.getLessBoomBimPlaceList()
}