package com.example.data.network.mypage

import com.example.data.network.auth.FcmTokenRequest
import com.example.data.network.mypage.request.PatchNickNameRequest
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.ProfileModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MyPageApi {

    @GET("/api/member")
    suspend fun getProfile(): Response<ProfileModel>

    @GET("/api/member/my-answer")
    suspend fun getMyAnswer() : Response<List<MyPageVoteResponse>>

    @GET("/api/member/my-question")
    suspend fun getMyQuestion() : Response<List<MyPageVoteResponse>>

    @PATCH("/api/member")
    suspend fun patchNickName(
        @Body request: PatchNickNameRequest
    ): Response<Unit>

    @POST("api/alarm/fcm-token")
    suspend fun sendFcmToken(
        @Body fcmToken: FcmTokenRequest
    ): Response<Unit>


}