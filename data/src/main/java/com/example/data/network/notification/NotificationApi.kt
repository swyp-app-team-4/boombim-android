package com.example.data.network.notification

import com.example.domain.model.NotificationModel
import com.example.domain.model.PlaceSearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface NotificationApi {

    @GET("/api/alarm/history")
    suspend fun getAlarmHistory(
        @Query("deviceType") deviceType: String
    ): Response<List<NotificationModel>>

    @PATCH("/api/alarm/status")
    suspend fun patchAlarm(): Response<Unit>

}