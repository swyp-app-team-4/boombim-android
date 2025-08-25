package com.example.data.repository

import android.adservices.ondevicepersonalization.RequestToken
import com.example.data.datastore.AppManageDataStore
import com.example.data.extension.covertApiResultToActionResultIfSuccess
import com.example.data.extension.covertApiResultToActionResultIfSuccessEmpty
import com.example.data.network.auth.AuthApi
import com.example.data.network.auth.request.RefreshTokenRequest
import com.example.data.network.safeFlow
import com.example.domain.datasource.AuthRemoteDataSource
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.SocialLoginSignUpResult
import com.example.domain.model.TokenModel
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthRemoteDataSource,
    private val appManageDataStore: AppManageDataStore,
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun socialLogin(
        provider: String,
        accessToken: String,
        refreshToken: String,
        expiresIn: Int,
        idToken: String
    ): ActionResult<SocialLoginSignUpResult> {
        val result = authDataSource.socialLogin(
            provider = provider,
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn,
            idToken = idToken
        )

        if (result is ApiResult.Success) {
            val body = result.data
            val access = body.accessToken
            val refresh = body.refreshToken

            access.let { appManageDataStore.saveAccessToken(it) }
            refresh.let { appManageDataStore.saveRefreshToken(it) }
        }
        return result.covertApiResultToActionResultIfSuccess()
    }

    override suspend fun postRefreshToken(refreshToken: String): Flow<ApiResult<TokenModel>> {
        return safeFlow {
            val request = RefreshTokenRequest(refreshToken)
            authApi.refreshToken(request)
        }
    }

    override suspend fun saveFcmToken(fcmToken: String, deviceType: String): ActionResult<*> {
        val result = authDataSource.saveFcmToken(fcmToken, deviceType)
        return result.covertApiResultToActionResultIfSuccessEmpty()
    }

}