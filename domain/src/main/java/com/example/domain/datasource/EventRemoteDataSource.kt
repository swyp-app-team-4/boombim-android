package com.example.domain.datasource

import com.example.domain.model.ApiResult
import com.example.domain.model.VoteErrorResponse

interface EventRemoteDataSource {

    suspend fun buyTicket(
        eventCampaignId: Int,
        amount: Int
    ) : ApiResult<VoteErrorResponse>
}