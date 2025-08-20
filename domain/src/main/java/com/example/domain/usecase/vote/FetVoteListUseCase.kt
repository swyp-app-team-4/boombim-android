package com.example.domain.usecase.vote

import com.example.domain.repository.VoteRepository
import javax.inject.Inject

class FetVoteListUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double) {
        voteRepository.getVoteList(latitude, longitude)
    }
}