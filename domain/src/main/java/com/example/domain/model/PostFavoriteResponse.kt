package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PostFavoriteResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: FavoriteDataId?
)

@Serializable
data class FavoriteDataId(
    val favoriteId: Int
)