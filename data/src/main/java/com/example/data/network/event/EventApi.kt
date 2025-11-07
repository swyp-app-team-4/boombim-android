package com.example.data.network.event

import com.example.data.network.event.request.BuyTicketRequest
import com.example.domain.model.VoteErrorResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

interface EventApi {

    @PATCH("api/app/point")
    suspend fun buyTicket(
        @Body body: BuyTicketRequest
    ) : Response<VoteErrorResponse>
}