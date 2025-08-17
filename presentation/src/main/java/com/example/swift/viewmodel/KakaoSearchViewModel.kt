package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.kakaosearch.FetchKakaoSearchUseCase
import com.example.domain.usecase.kakaosearch.GetKakaoSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KakaoSearchViewModel @Inject constructor(
    getKakaoSearchUseCase: GetKakaoSearchUseCase,
    private val fetchKakaoSearchUseCase: FetchKakaoSearchUseCase
): ViewModel() {

    val kakaoSearchList = getKakaoSearchUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    fun fetchKakaoSearch(
        query: String
    ){
        viewModelScope.launch {
            fetchKakaoSearchUseCase(query)
        }
    }
}