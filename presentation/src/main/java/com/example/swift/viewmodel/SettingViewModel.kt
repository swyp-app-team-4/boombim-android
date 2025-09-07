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
    private val updateFcmToken: UpdateFcmToken,
    private val appManageDataStore: AppManageDataStore,
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

    val notificationAllowed = appManageDataStore.getNotificationAllowed()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = true
        )

    fun updateNotificationAllowed(
        onSuccess: () -> Unit = {},
        onFail: (msg: String) -> Unit = {}
    ) {
        viewModelScope.launch {
            if (!notificationAllowed.value) {
                //등록 허용을 위해 먼저 허용으로 바꾼다.
                appManageDataStore.setNotificationAllowed(true)
                Firebase.messaging.token.addOnCompleteListener { task ->
                    viewModelScope.launch {
                        if (task.isSuccessful) {

                            val token = task.result
                            val result = updateFcmToken(token)
                            if (result is ActionResult.Success) {
                                onSuccess()
                            } else {
                                appManageDataStore.setNotificationAllowed(false)
                                onFail("Fail to Register Token")
                            }

                        } else {
                            appManageDataStore.setNotificationAllowed(false)
                            onFail("Fail to Get Token Info")
                        }
                    }
                }
            } else {
                appManageDataStore.setNotificationAllowed(false)
            }
        }
    }

    fun setNotificationAllowed(allowed: Boolean) {
        viewModelScope.launch {
            appManageDataStore.setNotificationAllowed(allowed)
        }
    }



}