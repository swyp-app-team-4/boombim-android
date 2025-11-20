package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class MemberPlaceResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: List<@Serializable(with = MemberPlaceDataSerializer::class) MemberPlaceData>
)

@Serializable
sealed class MemberPlaceData {
    @Serializable
    data class Place(
        val markerType: String,
        val placeId: Int,
        val name: String,
        val placeType: String,
        val coordinate: Coordinate,
        val congestionLevelName: String? = null,
        val congestionMessage: String? = null,
        val createdAt: String? = null,
        var isFavorite: Boolean,
        val isExpired: Boolean = false,
    ) : MemberPlaceData()

    @Serializable
    data class Cluster(
        val markerType: String,
        val coordinate: Coordinate,
        val clusterSize: Int,
        val congestionLevelCounts: Map<String, Int>
    ) : MemberPlaceData()
}

/**
 * type 필드를 기준으로 Place / Cluster 자동 분기
 */
object MemberPlaceDataSerializer :
    JsonContentPolymorphicSerializer<MemberPlaceData>(MemberPlaceData::class) {
    override fun selectDeserializer(element: JsonElement) = when (
        element.jsonObject["type"]?.jsonPrimitive?.contentOrNull
            ?: element.jsonObject["markerType"]?.jsonPrimitive?.contentOrNull
    ) {
        "PLACE" -> MemberPlaceData.Place.serializer()
        "CLUSTER" -> MemberPlaceData.Cluster.serializer()
        null -> throw IllegalArgumentException("Missing 'type' or 'markerType' field in MemberPlaceData: $element")
        else -> throw IllegalArgumentException("Unknown type: ${element.jsonObject["type"] ?: element.jsonObject["markerType"]}")
    }
}
