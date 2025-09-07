package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MemberPlaceDetailResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: MemberPlaceDetailData
)

@Serializable
data class MemberPlaceDetailData(
    val memberPlaceSummary: MemberPlaceSummary,
    val memberCongestionItems: List<MemberCongestionItem>,
    val hasNext: Boolean,
    val nextCursor: Int,
    val size: Int
)

@Serializable
data class MemberPlaceSummary(
    val memberPlaceId: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String?,
    val isFavorite: Boolean,
    val placeType: String
)

@Serializable
data class MemberCongestionItem(
    val memberCongestionId: Int,
    val memberProfile: String,
    val memberName: String,
    val congestionLevelName: String,
    val congestionLevelMessage: String,
    val createdAt: String
)
