package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ApiResult
import com.example.domain.usecase.FetVoteListUseCase
import com.example.domain.usecase.GetVoteListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val getVoteListUseCase: GetVoteListUseCase,
    private val fetchVoteListUseCase: FetVoteListUseCase
) : ViewModel() {

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

    val voteList = getVoteListUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )
}