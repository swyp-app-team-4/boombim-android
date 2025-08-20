package com.example.data.network.mypage

import com.example.domain.model.ProfileModel
import retrofit2.Response
import retrofit2.http.GET

interface MyPageApi {

    @GET("/api/member")
    suspend fun getProfile(): Response<ProfileModel>


}