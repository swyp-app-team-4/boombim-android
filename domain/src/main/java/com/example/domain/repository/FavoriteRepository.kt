package com.example.domain.repository

import com.example.domain.model.ActionResult
import com.example.domain.model.FavoriteData
import com.example.domain.model.PostFavoriteResponse
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun postFavorite(
        memberPlaceId: Int,
        placeType: String
    ) : ActionResult<PostFavoriteResponse>

    suspend fun deleteFavorite(
        memberPlaceId: Int,
        placeType: String
    ) : ActionResult<PostFavoriteResponse>

    suspend fun getFavorites()

    suspend fun getFavoriteList() : Flow<List<FavoriteData>>
}