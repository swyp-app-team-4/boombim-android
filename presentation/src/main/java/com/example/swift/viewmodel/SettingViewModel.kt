package com.example.swift.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datastore.AppManageDataStore
import com.example.domain.model.ActionResult
import com.example.domain.provider.TokenProvider
import com.example.domain.usecase.UpdateFcmToken
import com.example.domain.usecase.mypage.DeleteUserUseCase
import com.example.domain.usecase.mypage.LogoutUseCase
import com.example.domain.usecase.notification.PatchAlarmUseCase
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val provider: TokenProvider,
    private val appManageDataStore: AppManageDataStore,
    private val patchAlarmUseCase: PatchAlarmUseCase
): ViewModel() {

    fun logout(
        onSuccess: () -> Unit,
        onFail: (msg: String) -> Unit
    ) {
       viewModelScope.launch {
           val result = provider.getRefreshToken()?.let { logoutUseCase(it) }
           when(result){
               is ActionResult.Fail ->{
                     onFail(result.msg)
               }
               is ActionResult.Success -> {
                     provider.clearAllUserData()
                     onSuccess()
               }
               else -> {

               }
           }
       }
    }

    fun deleteUser(
        reason: String,
        onSuccess: (msg: String) -> Unit,
        onFail: () -> Unit
    ) {
        viewModelScope.launch {
            when(deleteUserUseCase(reason)){
                is ActionResult.Fail ->{
                    onFail()
                }
                is ActionResult.Success -> {
                    provider.clearAllUserData()
                    onSuccess("탈퇴가 완료되었습니다.")
                }
                else -> {

                }
            }
        }
    }

    fun patchAlarm(
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        viewModelScope.launch {
            val result = patchAlarmUseCase()
            when(result){
                is ActionResult.Fail ->{
                    onFail()
                }
                is ActionResult.Success -> {
                    onSuccess()
                }
            }
        }
    }

    val notificationAllowed = appManageDataStore.getNotificationAllowed()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = true
        )

    fun setNotificationAllowed(allowed: Boolean) {
        viewModelScope.launch {
            appManageDataStore.setNotificationAllowed(allowed)
        }
    }



}