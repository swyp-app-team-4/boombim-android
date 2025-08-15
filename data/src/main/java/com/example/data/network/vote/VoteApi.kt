package com.example.data.network.vote

import com.example.domain.model.VoteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VoteApi {
    @GET("/api/vote")
    suspend fun getVoteList(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<VoteResponse>
}