package com.example.data.network.map

import com.example.data.network.map.request.PostViewPortRequest
import com.example.domain.model.CongestionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MapApi {

    @POST("/official-places")
    suspend fun postViewPort(
        @Body viewport: PostViewPortRequest
    ): Response<CongestionResponse>

}