package com.example.data.datasource

import com.example.data.network.auth.AuthApi
import com.example.data.network.auth.FcmTokenRequest
import com.example.data.network.auth.request.SignUpRequest
import com.example.data.network.mypage.MyPageApi
import com.example.data.network.safeFlow
import com.example.domain.datasource.AuthRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.SocialLoginSignUpResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authApi: AuthApi,
    private val myPageApi: MyPageApi
) : AuthRemoteDataSource {

    override suspend fun socialLogin(
        provider: String,
        accessToken: String,
        refreshToken: String,
        expiresIn: Int,
        idToken: String
    ): ApiResult<SocialLoginSignUpResult> {
        val request = SignUpRequest(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn,
            idToken = idToken
        )
        return safeFlow { authApi.naverLogin(provider, request) }.first()
    }

    override suspend fun saveFcmToken(token: String, deviceType: String): ApiResult<*> {
        val request = FcmTokenRequest(token = token, deviceType = deviceType)
        return safeFlow { myPageApi.sendFcmToken(request) }.first()
    }
}