package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.usecase.FetVoteListUseCase
import com.example.domain.usecase.GetVoteListUseCase
import com.example.domain.usecase.PostVoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    getVoteListUseCase: GetVoteListUseCase,
    private val fetchVoteListUseCase: FetVoteListUseCase,
    private val postVoteUseCase: PostVoteUseCase
) : ViewModel() {

    val voteList = getVoteListUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    fun fetchVoteList(
        latitude: Double,
        longitude : Double
    ){
        viewModelScope.launch {
            fetchVoteListUseCase(
                latitude = latitude,
                longitude = longitude
            )
        }
    }


    fun postVote(
        voteId: Int,
        voteAnswerType: String,
        onSuccess: (msg: String) -> Unit,
        onFail: (msg: String) -> Unit
    ) {
        viewModelScope.launch{
            when (postVoteUseCase(voteId, voteAnswerType)) {
                is ApiResult.Success -> onSuccess("투표 성공")
                is ApiResult.SuccessEmpty -> onSuccess("투표 성공")
                is ApiResult.Fail.Error -> onFail("투표 실패")
                is ApiResult.Fail.Exception -> onFail("투표 실패")
            }
        }
    }


}