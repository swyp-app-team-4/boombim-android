package com.example.domain.repository

import com.example.domain.model.NotificationModel
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {


    fun getNotificationList(): Flow<List<NotificationModel>>

    /**
     * 알림목록 api를 불러온다.
     */
    suspend fun getNotice(deviceType: String)
}