package com.example.data.network.map

import com.example.data.network.map.request.PostViewPortRequest
import com.example.domain.model.CongestionResponse
import com.example.domain.model.OfficialPlaceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MapApi {

    @POST("/official-place")
    suspend fun postViewPort(
        @Body viewport: PostViewPortRequest
    ): Response<CongestionResponse>

    @GET("/official-place/{officialPlaceId}/overview")
    suspend fun getOfficialPlaceOverview(
        @Path("officialPlaceId") officialPlaceId: Int
    ): Response<OfficialPlaceResponse>


}