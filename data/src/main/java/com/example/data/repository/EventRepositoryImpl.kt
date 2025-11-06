package com.example.data.repository

import android.app.Notification.Action
import com.example.data.extension.covertApiResultToActionResultIfSuccess
import com.example.data.network.event.EventApi
import com.example.domain.datasource.EventRemoteDataSource
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.VoteErrorResponse
import com.example.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventRemoteDataSource: EventRemoteDataSource
) : EventRepository{

    override suspend fun buyTicket(eventCampaignId: Int, amount: Int): ActionResult<VoteErrorResponse> {
        val result = eventRemoteDataSource.buyTicket(eventCampaignId, amount)

        return result.covertApiResultToActionResultIfSuccess()
    }
}