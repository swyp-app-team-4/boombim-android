package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.VoteErrorResponse
import com.example.domain.model.VoteResponse
import kotlinx.coroutines.flow.Flow

interface VoteRemoteDataSource {

    suspend fun getVoteList(
        latitude: Double,
        longitude : Double,
    ): Flow<ApiResult<VoteResponse>>

    suspend fun postVote(
        voteId: Int,
        answer: String
    ):ApiResult<Unit>

    suspend fun patchVote(
        voteId: Int
    ):ApiResult<Unit>

    suspend fun makeVote(
        postId: Int,
        posLatitude: String,
        posLongitude: String,
        userLatitude: String,
        userLongitude: String,
        posName: String,
        address: String
    ): ApiResult<VoteErrorResponse>
}