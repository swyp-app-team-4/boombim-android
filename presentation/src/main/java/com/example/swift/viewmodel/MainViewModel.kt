package com.example.swift.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datastore.AppManageDataStore
import com.example.domain.provider.TokenProvider
import com.example.domain.usecase.notification.UpdateFcmToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val updateFcmToken: UpdateFcmToken,
    private val provider: TokenProvider,
    private val appManageDataStore: AppManageDataStore
) : ViewModel() {

    fun updateToken(token: String) {

        viewModelScope.launch {
//            appManageDataStore.clearAllUserData()
            val newAccess = provider.getAccessToken()
            val newRefresh = provider.getRefreshToken()
            Log.d("MainViewModel","AFTER CLEAR => $newAccess , $newRefresh")
        }

        viewModelScope.launch {
            updateFcmToken(token)
        }
    }

    fun setNotificationAllowed(allowed: Boolean) {
        viewModelScope.launch {
            appManageDataStore.setNotificationAllowed(allowed)
        }
    }

}