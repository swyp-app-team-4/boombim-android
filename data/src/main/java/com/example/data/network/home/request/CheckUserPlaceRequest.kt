package com.example.data.network.home.request

import kotlinx.serialization.Serializable

@Serializable
data class CheckUserPlaceRequest (
    val uuid: String,
    val name : String,
    val latitude: Double,
    val longitude: Double
)