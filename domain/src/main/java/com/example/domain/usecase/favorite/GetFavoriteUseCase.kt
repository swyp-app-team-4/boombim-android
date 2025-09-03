package com.example.domain.usecase.favorite

import com.example.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke() = favoriteRepository.getFavoriteList()
}