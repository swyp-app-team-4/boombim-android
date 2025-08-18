package com.example.domain.usecase

import com.example.domain.repository.VoteRepository
import javax.inject.Inject

class GetMyVoteListUseCase @Inject constructor(
    private val voteRepository: VoteRepository
){
    operator fun invoke() = voteRepository.getMyVoteList()
}