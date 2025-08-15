package com.example.domain.repository

import com.example.domain.model.NotificationModel
import com.example.domain.model.VoteItem
import kotlinx.coroutines.flow.Flow

interface VoteRepository {

    fun getVoteList(): Flow<List<VoteItem>>

    /**
     * 투표 목록 api를 불러온다.
     */
    suspend fun getVoteList(latitude: Double, longitude: Double)

}