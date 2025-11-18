package com.example.data.di

import android.util.Log
import com.example.data.datastore.AppManageDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val appManageDataStore: AppManageDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = TokenCache.accessToken ?: runBlocking {
            appManageDataStore.getAccessToken().first()
        }.also {
            TokenCache.accessToken = it
        }


        Log.d("AuthInterceptor", "사용하는 AccessToken = $token")

        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }

}