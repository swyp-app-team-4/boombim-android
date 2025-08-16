package com.example.data.datasource

import com.example.data.network.notification.NotificationApi
import com.example.data.network.notification.request.PostVoteRequest
import com.example.data.network.safeFlow
import com.example.data.network.vote.VoteApi
import com.example.domain.datasource.VoteRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.VoteResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class VoteRemoteDataSourceImpl @Inject constructor(
    private val voteApi: VoteApi
) : VoteRemoteDataSource {

    override suspend fun getVoteList(
        latitude: Double,
        longitude: Double
    ): Flow<ApiResult<VoteResponse>> {
        return safeFlow {
            voteApi.getVoteList(latitude, longitude)
        }
    }

    override suspend fun postVote(voteId: Int, answer: String): ApiResult<Unit> {
        val request = PostVoteRequest(voteId, answer)

        return safeFlow { voteApi.postVote(request)}.first()
    }
}