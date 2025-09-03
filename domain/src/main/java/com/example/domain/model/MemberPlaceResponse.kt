package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberPlaceResponse(
    @SerialName("code") val code: Int,
    @SerialName("status") val status: String,
    @SerialName("message") val message: String,
    @SerialName("data") val data: List<MemberPlaceData>
)

@Serializable
data class MemberPlaceData(
    @SerialName("type") val type: String,
    @SerialName("memberPlaceId") val memberPlaceId: Int,
    @SerialName("name") val name: String,
    @SerialName("coordinate") val coordinate: Coordinate,
    @SerialName("distance") val distance: Double,
    @SerialName("congestionLevelName") val congestionLevelName: String,
    @SerialName("congestionMessage") val congestionMessage: String,
    val clusterSize: Int? = null,
    @SerialName("isFavorite") var isFavorite: Boolean
)
