package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.MyVoteItem
import com.example.domain.model.TabType
import com.example.domain.usecase.FetVoteListUseCase
import com.example.domain.usecase.GetMyVoteListUseCase
import com.example.domain.usecase.GetVoteListUseCase
import com.example.domain.usecase.PatchVoteUseCase
import com.example.domain.usecase.PostVoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    getVoteListUseCase: GetVoteListUseCase,
    getMyVoteListUseCase: GetMyVoteListUseCase,
    private val fetchVoteListUseCase: FetVoteListUseCase,
    private val postVoteUseCase: PostVoteUseCase,
    private val patchVoteUseCase: PatchVoteUseCase
) : ViewModel() {

    private val _tabFilter = MutableStateFlow(TabType.ALL) // 현재 선택된 탭
    val tabFilter: StateFlow<TabType> = _tabFilter


    val voteList = getVoteListUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val myVoteList = getMyVoteListUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val filteredVoteList: StateFlow<List<MyVoteItem>> =
        combine(myVoteList, _tabFilter) { list, filter ->
            when (filter) {
                TabType.ALL -> list
                TabType.PROGRESS -> list.filter { it.voteStatus == "PROGRESS" }
                TabType.END -> list.filter { it.voteStatus == "END" }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    fun setTabFilter(tabType: TabType) {
        _tabFilter.value = tabType
    }

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

    fun patchVote(
        voteId: Int,
        onSuccess: (msg: String) -> Unit,
        onFail: (msg: String) -> Unit
    ){
        viewModelScope.launch{
            when (patchVoteUseCase(voteId)) {
                is ApiResult.Success -> onSuccess("투표가 종료되었습니다.")
                is ApiResult.SuccessEmpty -> onSuccess("투표가 종료되었습니다.")
                is ApiResult.Fail.Error -> onFail("투표 종료 실패")
                is ApiResult.Fail.Exception -> onFail("투표 종료 실패")
            }
        }
    }
}