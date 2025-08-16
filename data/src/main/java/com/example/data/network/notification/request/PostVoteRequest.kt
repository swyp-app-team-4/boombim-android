package com.example.data.network.notification.request

import kotlinx.serialization.Serializable

@Serializable
data class PostVoteRequest (
    private val voteId: Int,
    private val voteAnswerType: String
)