package com.example.data.network.kakaosearch

import com.example.domain.model.PlaceSearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoLocalApi {

    @GET("v2/local/search/keyword.json")
    @Headers("Authorization: KakaoAK e22ae8cd18b014bbeb1096fb69741bbf")
    suspend fun searchPlaces(
        @Query("query") query: String
    ): Response<PlaceSearchResponseDto>
}