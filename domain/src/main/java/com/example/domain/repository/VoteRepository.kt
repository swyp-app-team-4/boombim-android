package com.example.domain.repository

import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.MyVoteItem
import com.example.domain.model.NotificationModel
import com.example.domain.model.VoteErrorResponse
import com.example.domain.model.VoteItem
import kotlinx.coroutines.flow.Flow

interface VoteRepository {

    fun getVoteList(): Flow<List<VoteItem>>

    fun getMyVoteList(): Flow<List<MyVoteItem>>

    /**
     * 투표 목록 api를 불러온다.
     */
    suspend fun getVoteList(latitude: Double, longitude: Double)

    suspend fun postVote(voteId: Int, voteAnswerType: String): ApiResult<Unit>

    suspend fun patchVote(voteId: Int): ApiResult<Unit>

    suspend fun makeVote(
        postId: Int,
        posLatitude: String,
        posLongitude: String,
        userLatitude: String,
        userLongitude: String,
        posName: String
    ): ApiResult<VoteErrorResponse>
}