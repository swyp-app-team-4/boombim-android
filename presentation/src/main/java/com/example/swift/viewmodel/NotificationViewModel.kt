package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.FetchNotificationUseCase
import com.example.domain.usecase.GetNotificationListUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
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

    // 알림 전체 목록
    private val allNotifications = getNotificationListUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    //새소식 알림 목록
    val voteAndCommunicationNotifications = allNotifications
        .map { list ->
            list.filter { it.alarmType == "VOTE" || it.alarmType == "COMMUNICATION" }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    // 선택된 탭 상태
    private val _selectedType = MutableStateFlow("ANNOUNCEMENT") // 초기값 = 공지
    val selectedType = _selectedType.asStateFlow()

    fun selectTab(type: String) {
        _selectedType.value = type
    }

    // 탭에 맞는 필터링된 목록
    val filteredNotifications = combine(allNotifications, selectedType) { list, type ->
        list.filter { it.alarmType == type }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )



}