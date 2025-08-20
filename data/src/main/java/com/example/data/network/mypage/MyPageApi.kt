package com.example.data.network.mypage

import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.ProfileModel
import retrofit2.Response
import retrofit2.http.GET

interface MyPageApi {

    @GET("/api/member")
    suspend fun getProfile(): Response<ProfileModel>

    @GET("/api/member/my-answer")
    suspend fun getMyAnswer() : Response<List<MyPageVoteResponse>>

    @GET("/api/member/my-question")
    suspend fun getMyQuestion() : Response<List<MyPageVoteResponse>>


}