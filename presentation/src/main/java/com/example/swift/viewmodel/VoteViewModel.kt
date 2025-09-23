package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ApiResult
import com.example.domain.model.MyVoteItem
import com.example.domain.model.SortType
import com.example.domain.model.TabType
import com.example.domain.usecase.vote.FetVoteListUseCase
import com.example.domain.usecase.vote.GetMyVoteListUseCase
import com.example.domain.usecase.vote.GetVoteListUseCase
import com.example.domain.usecase.vote.MakeVoteUseCase
import com.example.domain.usecase.vote.PatchVoteUseCase
import com.example.domain.usecase.vote.PostVoteUseCase
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
    private val patchVoteUseCase: PatchVoteUseCase,
    private val makeVoteUseCase: MakeVoteUseCase
) : ViewModel() {

    private val _tabFilter = MutableStateFlow(TabType.ALL) // 현재 선택된 탭
    val tabFilter: StateFlow<TabType> = _tabFilter

    private val _sortType = MutableStateFlow(SortType.LATEST)
    val sortType: StateFlow<SortType> = _sortType

    fun setSortType(type: SortType) {
        _sortType.value = type
    }


    val voteList = getVoteListUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    private val myVoteList = getMyVoteListUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val filteredVoteList: StateFlow<List<MyVoteItem>> =
        combine(myVoteList, _tabFilter, _sortType) { list, filter, sort ->
            val filtered = when (filter) {
                TabType.ALL -> list
                TabType.PROGRESS -> list.filter { it.voteStatus == "PROGRESS" }
                TabType.END -> list.filter { it.voteStatus == "END" }
            }
            when (sort) {
                SortType.LATEST -> filtered.sortedByDescending { it.createdAt }
                SortType.OLDEST -> filtered.sortedBy { it.createdAt }
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
                is ApiResult.Fail.Error -> onFail("본인이 생성한 투표에는 참여할 수 없습니다.")
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

    fun makeVote(
        postId: Int,
        posLatitude: String,
        posLongitude: String,
        userLatitude: String,
        userLongitude: String,
        posName: String,
        address: String,
        onSuccess: () -> Unit,
        onFail: (msg: String) -> Unit
    ){
        viewModelScope.launch{
          val result = makeVoteUseCase(postId, posLatitude, posLongitude, userLatitude, userLongitude, posName, address)
            when(result){
                is ApiResult.Success -> onSuccess()
                is ApiResult.Fail.Error -> onFail(result.code.toString() ?: "투표 생성 실패")
                is ApiResult.Fail.Exception -> onFail("예상치 못한 오류")
                is ApiResult.SuccessEmpty ->  onSuccess()
            }
        }
    }
}