package com.example.domain.usecase

import com.example.domain.model.ApiResult
import com.example.domain.repository.VoteRepository
import javax.inject.Inject

class PostVoteUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(voteId: Int, voteAnswerType: String): ApiResult<Unit> {
        return voteRepository.postVote(voteId, voteAnswerType)
    }
}