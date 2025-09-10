package com.example.domain.repository

import com.example.domain.model.ActionResult
import com.example.domain.model.FavoriteData
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun postFavorite(
        memberPlaceId: Int,
        placeType: String
    ) : ActionResult<*>

    suspend fun deleteFavorite(
        memberPlaceId: Int,
        placeType: String
    ) : ActionResult<*>

    suspend fun getFavorites()

    fun getFavoriteList() : Flow<List<FavoriteData>>
}