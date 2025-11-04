package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PointHistoryResponse(
    val point: Int,
    val getPointHistoryRes: List<PointHistory>
)

@Serializable
data class PointHistory(
    val pointHistoryId: Int,
    val balance: Int,          // 거래 후 잔액
    val amount: Int,           // 거래량 (USE면 -, EARN이면 +)
    val createdAt: String,
    val pointAction: String,   // USE or EARN
    val pointCategory: String  // EVENT or CONGESTION 등
)
