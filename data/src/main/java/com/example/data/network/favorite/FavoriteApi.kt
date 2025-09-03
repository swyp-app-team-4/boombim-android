package com.example.data.network.favorite

import com.example.data.network.favorite.request.FavoriteRequest
import com.example.domain.model.PostFavoriteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface FavoriteApi {

    @POST("/favorite")
    suspend fun postFavorite(
        @Body body: FavoriteRequest
    ): Response<PostFavoriteResponse>

    @DELETE("/favorite")
    suspend fun deleteFavorite(
        @Query("memberPlaceId") memberPlaceId: Int
    ): Response<PostFavoriteResponse>
}