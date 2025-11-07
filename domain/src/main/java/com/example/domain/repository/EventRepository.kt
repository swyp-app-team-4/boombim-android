package com.example.domain.repository

import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.VoteErrorResponse
import kotlinx.coroutines.flow.Flow


interface EventRepository {

    suspend fun buyTicket(
        eventCampaignId: Int,
        amount: Int
    ): ActionResult<VoteErrorResponse>
}