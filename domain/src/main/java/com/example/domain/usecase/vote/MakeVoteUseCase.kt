package com.example.domain.usecase.vote

import com.example.domain.model.ApiResult
import com.example.domain.model.VoteErrorResponse
import com.example.domain.repository.VoteRepository
import javax.inject.Inject

class MakeVoteUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        postId: Int,
        posLatitude: String,
        posLongitude: String,
        userLatitude: String,
        userLongitude: String,
        posName: String,
        address: String
    ): ApiResult<VoteErrorResponse>{
        return voteRepository.makeVote(postId, posLatitude, posLongitude, userLatitude, userLongitude, posName, address)
    }
}