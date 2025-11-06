package com.example.data.datasource

import com.example.data.network.event.EventApi
import com.example.data.network.event.request.BuyTicketRequest
import com.example.data.network.safeFlow
import com.example.domain.datasource.EventRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.VoteErrorResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EventRemoteDataSourceImpl @Inject constructor(
    private val eventApi: EventApi
) : EventRemoteDataSource{

    override suspend fun buyTicket(
        eventCampaignId: Int,
        amount: Int
    ): ApiResult<VoteErrorResponse> {
        return safeFlow {
            val request = BuyTicketRequest(eventCampaignId, amount)
            eventApi.buyTicket(request)
        }.first()
    }

}