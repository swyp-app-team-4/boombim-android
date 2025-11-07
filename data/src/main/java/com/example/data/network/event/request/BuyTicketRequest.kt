package com.example.data.network.event.request

import kotlinx.serialization.Serializable

@Serializable
data class BuyTicketRequest (
    val eventCampaignId : Int,
    val amount: Int
)