package com.example.data.repository

import com.example.data.extension.covertApiResultToActionResultIfSuccess
import com.example.domain.datasource.VoteRemoteDataSource
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.MyVoteItem
import com.example.domain.model.NotificationModel
import com.example.domain.model.VoteItem
import com.example.domain.model.VoteResponse
import com.example.domain.model.onSuccess
import com.example.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteRemoteDataSource: VoteRemoteDataSource,
) : VoteRepository {

    /*
     * 내 주변 투표 목록을 불러온다
     */
    private val _voteList = MutableStateFlow(emptyList<VoteItem>())

    private val voteList
        get() = _voteList.asStateFlow()


    /*
    * 내 질문 목록을 불러온다
    */
    private val _myVoteList = MutableStateFlow(emptyList<MyVoteItem>())

    private val myVoteList
        get() = _myVoteList.asStateFlow()

    override fun getVoteList(): Flow<List<VoteItem>>  = voteList

    override suspend fun getVoteList(latitude: Double, longitude: Double) {
        voteRemoteDataSource.getVoteList(latitude, longitude).first().let { result ->
            if (result is ApiResult.Success) {
                _voteList.update {
                    result.data.voteResList
                }
                _myVoteList.update {
                    result.data.myVoteResList
                }
            }
        }
    }

    override suspend fun postVote(
        voteId: Int,
        voteAnswerType: String
    ): ApiResult<Unit> {
        return try {
            when (val response = voteRemoteDataSource.postVote(voteId, voteAnswerType)) {
                is ApiResult.Success -> ApiResult.Success(Unit)
                is ApiResult.SuccessEmpty -> ApiResult.SuccessEmpty
                is ApiResult.Fail.Error -> ApiResult.Fail.Error(response.code, response.message)
                is ApiResult.Fail.Exception -> ApiResult.Fail.Exception(response.e)
            }
        } catch (e: Exception) {
            ApiResult.Fail.Exception(e)
        }
    }


}