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
    ): ApiResult<SocialLoginSignUpResult> {
        return try {
            when (val response =
                authDataSource.socialLogin(provider, accessToken, refreshToken, expiresIn, idToken)
            ) {
                is ApiResult.Success -> {
                    appManageDataStore.saveAccessToken(response.data.accessToken)
                    appManageDataStore.saveRefreshToken(response.data.refreshToken)
                    ApiResult.Success(response.data)
                }

                is ApiResult.SuccessEmpty -> ApiResult.SuccessEmpty
                is ApiResult.Fail.Error -> ApiResult.Fail.Error(response.code, response.message)
                is ApiResult.Fail.Exception -> ApiResult.Fail.Exception(response.e)
            }
        } catch (e: Exception) {
            ApiResult.Fail.Exception(e)
        }
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