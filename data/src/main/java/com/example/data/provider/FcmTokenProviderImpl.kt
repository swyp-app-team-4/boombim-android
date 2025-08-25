package com.example.data.provider

import com.example.data.datastore.AppManageDataStore
import com.example.domain.provider.FcmTokenProvider
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class FcmTokenProviderImpl @Inject constructor(
    private val appDataStore: AppManageDataStore
) : FcmTokenProvider {

    override suspend fun getSavedFcmToken(): String? {
        return appDataStore.getSavedFcmToken().firstOrNull()
    }

    override suspend fun setSavedFcmToken(token: String?) {
        appDataStore.setSavedFcmToken(token)
    }

}