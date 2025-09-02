package com.example.data.network.favorite.request

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRequest (
    val memberPlaceId: Int
)