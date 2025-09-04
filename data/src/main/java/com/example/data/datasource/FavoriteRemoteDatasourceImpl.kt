package com.example.data.datasource

import com.example.data.network.favorite.FavoriteApi
import com.example.data.network.favorite.request.FavoriteRequest
import com.example.data.network.safeFlow
import com.example.domain.datasource.FavoriteRemoteDatasource
import com.example.domain.model.ApiResult
import com.example.domain.model.FavoriteResponse
import com.example.domain.model.PostFavoriteResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FavoriteRemoteDatasourceImpl @Inject constructor(
    private val favoriteApi: FavoriteApi
) : FavoriteRemoteDatasource{

    override suspend fun postFavorite(memberPlaceId: Int, placeType: String): ApiResult<PostFavoriteResponse> {
        val request = FavoriteRequest(placeType, memberPlaceId)

        return safeFlow { favoriteApi.postFavorite(request) }.first()
    }

    override suspend fun deleteFavorite(memberPlaceId: Int, placeType: String): ApiResult<PostFavoriteResponse> {
        return safeFlow { favoriteApi.deleteFavorite(memberPlaceId, placeType)}.first()
    }

    override suspend fun getFavorites(): Flow<ApiResult<FavoriteResponse>> {
        return safeFlow { favoriteApi.getFavorites()}
    }

}