package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileModel (
    var name: String = "",
    val profile: String = "",
    val email: String = "",
    val socialProvider: String = "",
    val voteCnt: Int = 0,
    val questionCnt: Int = 0
)