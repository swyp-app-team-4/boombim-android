package com.example.data.di

import com.example.data.datastore.AppManageDataStore
import com.example.data.network.auth.AuthApi
import com.example.data.network.auth.request.RefreshTokenRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val appManageDataStore: AppManageDataStore,
    private val authApi: AuthApi
) : Authenticator {
    private val tokenMutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null
        if (responseCount(response) >= 2) return null

        val newAccessToken = runBlocking {
            tokenMutex.withLock {
                // 이미 최신 토큰인지 확인
                val currentToken = appManageDataStore.getAccessToken().first()
                if (currentToken != null && currentToken != response.request.header("Authorization")?.removePrefix("Bearer ")) {
                    currentToken
                } else {
                    val refreshToken = appManageDataStore.getRefreshToken().first() ?: return@runBlocking null
                    val refreshResponse = authApi.refreshToken(RefreshTokenRequest(refreshToken))
                    if (refreshResponse.isSuccessful) {
                        val tokenModel = refreshResponse.body() ?: return@runBlocking null
                        appManageDataStore.saveAccessToken(tokenModel.accessToken)
                        appManageDataStore.saveRefreshToken(tokenModel.refreshToken)
                        tokenModel.accessToken
                    } else null
                }
            }
        } ?: return null

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }

}