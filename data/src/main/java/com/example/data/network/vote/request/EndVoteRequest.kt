package com.example.data.network.vote.request

import kotlinx.serialization.Serializable

@Serializable
data class EndVoteRequest (
    val voteId: Int
)