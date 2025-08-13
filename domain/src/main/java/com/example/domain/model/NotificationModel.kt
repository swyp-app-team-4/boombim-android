package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationModel (
    val title : String,
    val message: String,
    val alarmType: String,
    val deliveryStatus: String
)