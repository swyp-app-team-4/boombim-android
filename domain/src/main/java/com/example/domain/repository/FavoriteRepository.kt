package com.example.domain.repository

import com.example.domain.model.ActionResult
import com.example.domain.model.PostFavoriteResponse

interface FavoriteRepository {

    suspend fun postFavorite(
        memberPlaceId: Int
    ) : ActionResult<PostFavoriteResponse>
}