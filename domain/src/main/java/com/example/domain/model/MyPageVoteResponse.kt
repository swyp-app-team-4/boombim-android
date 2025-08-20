package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MyPageVoteResponse(
    val day: String, // 날짜 (예: "2025-08-19T00:00:00")
    val res: List<PopularityDetail>
)

@Serializable
data class PopularityDetail(
    val voteId: Int,
    val day: String, // 투표 시간 (예: "2025-08-19T16:31:06.979297")
    val posName: String, // 위치 이름 (예: "구로역")
    val popularStatus: String, // 혼잡도 상태 (예: "RELAXED")
    val popularCnt: Int // 투표 수
)
