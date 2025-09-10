package com.example.data.network.home.request

import kotlinx.serialization.Serializable

@Serializable
data class MakeAutoCongestionMessageRequest (
    val memberPlaceName: String,
    val congestionLevelName: String,
    val congestionMessage: String,
)