package com.example.data.network.mypage

import com.example.data.network.auth.FcmTokenRequest
import com.example.data.network.auth.request.RefreshTokenRequest
import com.example.data.network.mypage.request.DeleteAccountRequest
import com.example.data.network.mypage.request.PatchNickNameRequest
import com.example.domain.model.EventCampaign
import com.example.domain.model.MyActivityResponse
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.PatchProfileImageResponse
import com.example.domain.model.PointHistoryResponse
import com.example.domain.model.ProfileModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface MyPageApi {

    @GET("/api/app/member")
    suspend fun getProfile(): Response<ProfileModel>

    @PATCH("/api/app/member/name")
    suspend fun patchNickName(
        @Body request: PatchNickNameRequest
    ): Response<Unit>

    @POST("api/app/oauth2/logout")
    suspend fun logout(
        @Body request: RefreshTokenRequest
    ): Response<Unit>

    @POST("api/alarm/fcm-token")
    suspend fun sendFcmToken(
        @Body fcmToken: FcmTokenRequest
    ): Response<Unit>

    @Multipart
    @PATCH("/api/app/member/profile")
    suspend fun patchProfileImage(
        @Part multipartFile: MultipartBody.Part
    ): Response<PatchProfileImageResponse>

    @POST("/api/app/member")
    suspend fun deleteAccount(
        @Body request: DeleteAccountRequest
    ): Response<Unit>

    @GET("/api/app/member/congestion")
    suspend fun getMyActivity(): Response<List<MyActivityResponse>>

    //////////////////////////////////////// 포인트

    @GET("/api/app/point")
    suspend fun getMyPoint() : Response<PointHistoryResponse>

    @GET("/api/app/event")
    suspend fun getEvent() : Response<EventCampaign>


}