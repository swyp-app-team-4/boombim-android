package com.example.domain.repository

import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.SocialLoginSignUpResult
import com.example.domain.model.TokenModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun socialLogin(
        provider: String,
        accessToken: String,
        refreshToken: String,
        expiresIn: Int,
        idToken: String
    ): ApiResult<SocialLoginSignUpResult>

    /**
     *  토큰을 갱신한다
     */
    suspend fun postRefreshToken(
        refreshToken: String
    ): Flow<ApiResult<TokenModel>>

    suspend fun saveFcmToken(fcmToken: String, deviceType: String): ActionResult<*>
}