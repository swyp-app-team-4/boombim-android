package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ProfileModel
import com.example.domain.usecase.mypage.FetMyPageAnswerListUseCase
import com.example.domain.usecase.mypage.FetchMyPageQuestionListUseCase
import com.example.domain.usecase.mypage.FetchMyProfileUseCase
import com.example.domain.usecase.mypage.GetMyAnswerUseCase
import com.example.domain.usecase.mypage.GetMyProfileUseCase
import com.example.domain.usecase.mypage.GetMyQuestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    fetchMyProfileUseCase: FetchMyProfileUseCase,
    getMyProfileUseCase: GetMyProfileUseCase,
    fetchMyPageQuestionListUseCase: FetchMyPageQuestionListUseCase,
    getMyAnswerUseCase: GetMyAnswerUseCase,
    getMyQuestionUseCase: GetMyQuestionUseCase,
    fetchMYPageAnswerListUseCase: FetMyPageAnswerListUseCase

): ViewModel(){

    init {
        viewModelScope.launch {
            fetchMyProfileUseCase()
            fetchMyPageQuestionListUseCase()
            fetchMYPageAnswerListUseCase()
        }
    }

    val profile = getMyProfileUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProfileModel()
        )

    val myAnswer = getMyAnswerUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val myQuestion = getMyQuestionUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )



}