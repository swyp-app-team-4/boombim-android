package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.NotificationModel
import com.example.domain.model.SocialLoginSignUpResult
import kotlinx.coroutines.flow.Flow

interface NotificationRemoteDataSource {
    suspend fun getNotification(deviceType: String): Flow<ApiResult<List<NotificationModel>>>

    suspend fun patchAlarm(): ApiResult<*>
}