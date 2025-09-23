package com.example.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RegionResponse (
    val tempId: String = UUID.randomUUID().toString(),
    val regionDate: String,
    val startTime: String,
    val endTime: String,
    val posName: String,
    val area: String,
    val peopleCnt: Int
)