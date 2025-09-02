package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PostFavoriteResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: FavoriteData
)

@Serializable
data class FavoriteData(
    val favoriteId: Int
)