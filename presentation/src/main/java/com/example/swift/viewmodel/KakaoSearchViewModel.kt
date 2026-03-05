package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.PlaceDocumentDto
import com.example.domain.usecase.kakaosearch.FetchKakaoSearchUseCase
import com.example.domain.usecase.kakaosearch.GetKakaoSearchUseCase
import com.example.domain.usecase.kakaosearch.SearchKakaoPlaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KakaoSearchViewModel @Inject constructor(
    getKakaoSearchUseCase: GetKakaoSearchUseCase,
    private val fetchKakaoSearchUseCase: FetchKakaoSearchUseCase,
    private val searchKakaoPlaceUseCase: SearchKakaoPlaceUseCase
): ViewModel() {

    private val queryFlow = MutableStateFlow("")

    val kakaoSearchPaging =
        queryFlow
            .debounce(300)
            .filter { it.isNotBlank() }
            .flatMapLatest { query ->
                searchKakaoPlaceUseCase(query)
            }
            .cachedIn(viewModelScope)

    fun search(query: String) {
        queryFlow.value = query
    }

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