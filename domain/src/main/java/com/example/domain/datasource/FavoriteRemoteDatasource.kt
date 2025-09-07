package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.FavoriteResponse
import kotlinx.coroutines.flow.Flow

interface FavoriteRemoteDatasource {

    suspend fun postFavorite(
        memberPlaceId: Int,
        placeType: String
    ) : ApiResult<Unit>

    suspend fun deleteFavorite(
        memberPlaceId: Int,
        placeType: String
    ) : ApiResult<Unit>

    suspend fun getFavorites() : Flow<ApiResult<FavoriteResponse>>
}