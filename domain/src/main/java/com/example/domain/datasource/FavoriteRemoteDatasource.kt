package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.PostFavoriteResponse

interface FavoriteRemoteDatasource {

    suspend fun postFavorite(
        memberPlaceId: Int
    ) : ApiResult<PostFavoriteResponse>

    suspend fun deleteFavorite(
        memberPlaceId: Int
    ) : ApiResult<PostFavoriteResponse>
}