package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MyPageVoteResponse(
    val day: String, // 날짜 (예: "2025-08-26T12:39:48.199Z")
    val res: List<PopularityDetail>
)

@Serializable
data class PopularityDetail(
    val voteId: Int,
    val profile: List<String>, // 프로필 이미지 URL
    val day: String, // 투표 시간 (예: "2025-08-26T12:39:48.201Z")
    val posName: String, // 위치 이름 (예: "강남 교보문고")
    val popularRes: List<String>, // 혼잡도 상태 (예: "CROWED")
    val relaxedCnt: Int,
    val commonly: Int,
    val slightlyBusyCnt: Int,
    val crowedCnt: Int,
    val voteAllCnt: Int,
    val voteStatus: String
)
