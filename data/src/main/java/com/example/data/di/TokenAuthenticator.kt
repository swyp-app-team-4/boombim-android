package com.example.data.di

import android.util.Log
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

    // 여러 스레드에서 동시에 토큰 갱신을 요청하는 경우 한 번만 실행되도록 보호
    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            Log.w("TokenAuthenticator", "Too many retry attempts.")
            return null
        }

        return runBlocking {
            mutex.withLock {
                val currentAccessToken = appManageDataStore.getAccessToken().first()
                val requestAccessToken = response.request.header("Authorization")?.removePrefix("Bearer ")

                // 이미 최신 토큰으로 재시도 중이라면 null 리턴 (무한루프 방지)
                if (currentAccessToken == requestAccessToken) {
                    Log.w("TokenAuthenticator", "Access token already updated, skipping re-authentication.")
                    val newRefreshToken = appManageDataStore.getRefreshToken().first()
                    if (newRefreshToken.isNullOrEmpty()) return@withLock null
                }

                val refreshToken = appManageDataStore.getRefreshToken().first()
                if (refreshToken.isNullOrEmpty()) {
                    Log.e("TokenAuthenticator", "No refresh token found.")
                    return@withLock null
                }

                Log.d("TokenAuthenticator", "Refreshing access token...")

                val tokenResponse = try {
                    val res = authApi.refreshToken(RefreshTokenRequest(refreshToken))
                    if (res.isSuccessful) res.body() else null
                } catch (e: Exception) {
                    Log.e("TokenAuthenticator", "Token refresh failed: ${e.message}")
                    null
                }

                Log.d("TokenAuthenticator", "Token refresh response: $tokenResponse")

                if (tokenResponse == null) {
                    Log.e("TokenAuthenticator", "Failed to refresh token. Response was null.")
                    return@withLock null
                }

                // 새 토큰 저장
                appManageDataStore.saveAccessToken(tokenResponse.accessToken)
                appManageDataStore.saveRefreshToken(tokenResponse.refreshToken)
                TokenCache.accessToken = tokenResponse.accessToken

                Log.d("TokenAuthenticator", "New access token applied.")

                // 새 access token으로 요청 재시도
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${tokenResponse.accessToken}")
                    .build()
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var res = response.priorResponse
        var count = 1
        while (res != null) {
            count++
            res = res.priorResponse
        }
        return count
    }
}
