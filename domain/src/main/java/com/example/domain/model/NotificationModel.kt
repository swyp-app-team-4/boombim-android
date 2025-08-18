package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationModel (
    val alarmReId: Int,
    val title : String,
    val alarmType: String,
    val alarmTime: String,
    val deliveryStatus: String
)