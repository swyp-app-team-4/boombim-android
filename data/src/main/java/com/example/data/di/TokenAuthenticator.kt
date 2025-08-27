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

    override fun authenticate(route: Route?, response: Response): Request? {
        // 이미 재시도한 요청인지 확인 (무한 루프 방지)
        if (responseCount(response) >= 2) return null

        // 현재 refresh token 가져오기 (동기 방식으로)
        val refreshToken = runBlocking {
            appManageDataStore.getRefreshToken().first()
        } ?: return null

        // 재발급 API 호출 (동기)
        val tokenResponse = runBlocking {
            try {
                val res = authApi.refreshToken(RefreshTokenRequest(refreshToken))
                if (res.isSuccessful) res.body() else null
            } catch (e: Exception) {
                null
            }
        } ?: return null

        // 새 토큰 저장
        runBlocking {
            Log.d("TokenAuthenticator", "New Access Token: ${tokenResponse.accessToken}")
            appManageDataStore.saveAccessToken(tokenResponse.accessToken)
            appManageDataStore.saveRefreshToken(tokenResponse.refreshToken)
        }

        // 기존 요청을 새로운 access token으로 재시도
        return response.request.newBuilder()
            .header("Authorization", "Bearer ${tokenResponse.accessToken}")
            .build()
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
