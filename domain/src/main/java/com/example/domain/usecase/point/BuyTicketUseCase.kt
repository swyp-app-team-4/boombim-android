package com.example.domain.usecase.point

import android.util.Log
import com.example.domain.model.ActionResult
import com.example.domain.model.VoteErrorResponse
import com.example.domain.repository.EventRepository
import com.example.domain.repository.MyPageRepository
import javax.inject.Inject

class BuyTicketUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): ActionResult<VoteErrorResponse> {
        val result = eventRepository.buyTicket(2, 20)
        if (result is ActionResult.Success) {
            myPageRepository.deletePoint(20)
        }
        return result
    }
}