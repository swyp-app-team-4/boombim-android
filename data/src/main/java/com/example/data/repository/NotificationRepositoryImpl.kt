package com.example.data.repository

import com.example.domain.datasource.NotificationRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.NotificationModel
import com.example.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
) : NotificationRepository {

    // 공지 목록을 저장하는 StateFlow
    private val _notificationList = MutableStateFlow(emptyList<NotificationModel>())

    private val notificationList
        get() = _notificationList.asStateFlow()


    override fun getNotificationList(): Flow<List<NotificationModel>> = notificationList

    override suspend fun getNotice(deviceType: String) {
        notificationRemoteDataSource.getNotification(deviceType).first().let { result ->
            if (result is ApiResult.Success){
                _notificationList.update {
                    result.data
                }
            }
        }
    }
}