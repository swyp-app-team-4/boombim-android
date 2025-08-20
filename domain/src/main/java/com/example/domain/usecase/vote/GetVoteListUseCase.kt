package com.example.domain.usecase.vote

import com.example.domain.repository.VoteRepository
import javax.inject.Inject

class GetVoteListUseCase @Inject constructor(
    private val voteRepository: VoteRepository
){
    operator fun invoke() = voteRepository.getVoteList()
}