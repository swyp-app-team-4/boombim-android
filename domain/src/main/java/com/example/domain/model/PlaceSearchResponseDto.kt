package com.example.domain.model

import kotlinx.serialization.Serializable

// 응답 meta 정보
@Serializable
data class MetaDto(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean,
    val same_name: RegionInfoDto?
)

// 지역 정보 (선택적으로 제공)
@Serializable
data class RegionInfoDto(
    val region: List<String>,
    val keyword: String,
    val selected_region: String
)

// 장소 문서 (결과 리스트)
@Serializable
data class PlaceDocumentDto(
    val id: String,
    val place_name: String,
    val category_name: String,
    val category_group_code: String?,
    val category_group_name: String?,
    val phone: String?,
    val address_name: String?,
    val road_address_name: String?,
    val x: String, // longitude
    val y: String, // latitude
    val place_url: String?,
    val distance: String? // 중심좌표 기준 거리 (미터)
)

@Serializable
data class PlaceSearchResponseDto(
    val meta: MetaDto,
    val documents: List<PlaceDocumentDto>
)
