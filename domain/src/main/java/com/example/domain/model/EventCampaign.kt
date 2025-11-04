package com.example.domain.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class EventCampaign(
    val eventCampaignId: Int? = 0,
    val eventStartDate: String? = "",
    val eventEndDate: String? = "",
    val winnerAnnouncementDate: String? = "",
    val memberPoint: Int? = 0,
    val currentTicket: Int? = 0
)