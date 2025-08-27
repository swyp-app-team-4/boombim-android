package com.example.data.provider

import com.example.data.datastore.AppManageDataStore
import com.example.domain.provider.TokenProvider
import jakarta.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class TokenProviderImpl @Inject constructor(
    private val appDataStore: AppManageDataStore
) : TokenProvider {

    override fun getAccessToken(): String? {
        return runBlocking {
            appDataStore.getAccessToken().firstOrNull()
        }
    }

    override fun getRefreshToken(): String? {
        return runBlocking {
            appDataStore.getRefreshToken().firstOrNull()
        }
    }

    override suspend fun clearAllUserData() {
        appDataStore.clearAllUserData()
    }


}