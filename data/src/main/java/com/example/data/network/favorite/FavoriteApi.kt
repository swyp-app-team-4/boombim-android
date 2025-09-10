package com.example.data.network.favorite

import com.example.data.network.favorite.request.FavoriteRequest
import com.example.domain.model.FavoriteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FavoriteApi {

    @POST("/favorite")
    suspend fun postFavorite(
        @Body body: FavoriteRequest
    ): Response<Unit>

    @DELETE("/favorite")
    suspend fun deleteFavorite(
        @Query("placeId") memberPlaceId: Int,
        @Query("placeType") placeType: String
    ): Response<Unit>

    @GET("/favorite")
    suspend fun getFavorites(): Response<FavoriteResponse>
}