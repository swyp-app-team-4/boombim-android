package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.FetchNotificationUseCase
import com.example.domain.usecase.GetNotificationListUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    getNotificationListUseCase: GetNotificationListUseCase,
    fetchNotificationUseCase: FetchNotificationUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            fetchNotificationUseCase("ANDROID")
        }
    }

    // 알림화면 알림 목록
    val notificationList = getNotificationListUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )
}