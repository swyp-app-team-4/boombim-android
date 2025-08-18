package com.example.data.network.vote

import com.example.data.network.auth.request.SignUpRequest
import com.example.data.network.notification.request.PostVoteRequest
import com.example.data.network.vote.request.EndVoteRequest
import com.example.domain.model.PlaceSearchResponseDto
import com.example.domain.model.VoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface VoteApi {

    /**
     * 투표 목록을 불러오는 API
     */
    @GET("/api/vote")
    suspend fun getVoteList(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<VoteResponse>

    /**
     * 투표 목록을 불러오는 API
     */
    @POST("/api/vote/answer")
    suspend fun postVote(
        @Body data: PostVoteRequest
    ): Response<Unit>

    /**
     * 투표 종료 API
     */
    @PATCH("/api/vote")
    suspend fun closeVote(
        @Body request: EndVoteRequest
    ): Response<Unit>

}