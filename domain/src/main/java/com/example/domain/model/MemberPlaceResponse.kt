package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
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
        val type: String,
        val memberPlaceId: Int,
        val name: String,
        val placeType: String,
        val coordinate: Coordinate,
        val distance: Double,
        val congestionLevelName: String,
        val congestionMessage: String,
        val createdAt: String,
        var isFavorite: Boolean
    ) : MemberPlaceData()

    @Serializable
    data class Cluster(
        val type: String,
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
    override fun selectDeserializer(element: JsonElement) =
        when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            "PLACE" -> MemberPlaceData.Place.serializer()
            "CLUSTER" -> MemberPlaceData.Cluster.serializer()
            else -> throw IllegalArgumentException("Unknown type: ${element.jsonObject["type"]}")
        }
}
