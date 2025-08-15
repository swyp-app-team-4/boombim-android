package com.example.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class VoteResponse(
    val voteResList: List<VoteItem>,
    val myVoteResList: List<MyVoteItem>
)

@Serializable
data class VoteItem(
    val voteId: Int,
    val voteDuplicationCnt: Int,
    val createdAt: String,
    val posName: String,
    val relaxedCnt: Int,
    val commonly: Int,
    val slightlyBusyCnt: Int,
    val crowedCnt: Int,
    val allType: String,
    @Transient var selectedIcon: Int = -1
)

@Serializable
data class MyVoteItem(
    val voteId: Int,
    val voteDuplicationCnt: Int,
    val createdAt: String,
    val posName: String,
    val relaxedCnt: Int,
    val commonly: Int,
    val slightlyBusyCnt: Int,
    val crowedCnt: Int,
    val allType: String,
    val voteStatus: String
)
