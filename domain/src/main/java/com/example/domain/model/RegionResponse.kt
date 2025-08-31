package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RegionResponse (
    val regionDate: String,
    val startTime: String,
    val endTime: String,
    val posName: String,
    val area: String,
    val peopleCnt: Int
)