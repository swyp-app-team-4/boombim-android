package com.example.data.datasource

import com.example.data.network.notification.NotificationApi
import com.example.data.network.safeFlow
import com.example.domain.datasource.NotificationRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.NotificationModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRemoteDataSourceImpl @Inject constructor(
    private val notificationApi: NotificationApi
) : NotificationRemoteDataSource {

    override suspend fun getNotification(deviceType: String): Flow<ApiResult<List<NotificationModel>>> {
        return safeFlow { notificationApi.getAlarmHistory(deviceType) }
    }
}