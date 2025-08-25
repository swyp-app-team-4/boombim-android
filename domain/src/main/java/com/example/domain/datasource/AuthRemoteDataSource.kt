package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.SocialLoginSignUpResult

interface AuthRemoteDataSource {

    suspend fun socialLogin(
        provider: String,
        accessToken: String,
        refreshToken: String,
        expiresIn: Int,
        idToken: String
    ): ApiResult<SocialLoginSignUpResult>

    suspend fun saveFcmToken(token: String, deviceType: String): ApiResult<*>
}