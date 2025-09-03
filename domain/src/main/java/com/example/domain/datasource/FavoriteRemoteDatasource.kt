package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.FavoriteResponse
import com.example.domain.model.PostFavoriteResponse
import kotlinx.coroutines.flow.Flow

interface FavoriteRemoteDatasource {

    suspend fun postFavorite(
        memberPlaceId: Int
    ) : ApiResult<PostFavoriteResponse>

    suspend fun deleteFavorite(
        memberPlaceId: Int
    ) : ApiResult<PostFavoriteResponse>

    suspend fun getFavorites() : Flow<ApiResult<FavoriteResponse>>
}