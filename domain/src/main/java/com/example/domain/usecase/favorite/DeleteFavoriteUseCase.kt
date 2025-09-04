package com.example.domain.usecase.favorite

import com.example.domain.repository.FavoriteRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
){
    suspend operator fun invoke(
        memberPlaceId: Int,
        placeType: String
    ) = favoriteRepository.deleteFavorite(memberPlaceId, placeType)
}