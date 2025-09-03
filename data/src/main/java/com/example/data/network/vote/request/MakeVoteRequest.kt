package com.example.data.network.vote.request

import kotlinx.serialization.Serializable

@Serializable
data class MakeVoteRequest (
    val posId : Int,
    val posLatitude: String,
    val posLongitude: String,
    val userLatitude: String,
    val userLongitude: String,
    val posName: String,
    val address: String
)