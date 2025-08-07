package com.example.data.network.auth

import com.example.data.network.auth.request.SignUpRequest
import com.example.domain.model.SocialLoginSignUpResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {

    @POST("api/oauth2/login/{provider}")
    suspend fun naverLogin(
        @Path("provider") provider: String,
        @Body data: SignUpRequest
    ): Response<SocialLoginSignUpResult>

}