package com.example.data.network.home

import com.example.domain.model.RegionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {

    @GET("api/region")
    suspend fun getRegionList(
        @Query("date") query: String
    ): Response<List<RegionResponse>>
}