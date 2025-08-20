package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ProfileModel
import com.example.domain.usecase.FetchMyProfileUseCase
import com.example.domain.usecase.GetMyProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    fetchMyProfileUseCase: FetchMyProfileUseCase,
    getMyProfileUseCase: GetMyProfileUseCase
): ViewModel(){

    init {
        viewModelScope.launch {
            fetchMyProfileUseCase()
        }
    }

    val profile = getMyProfileUseCase()
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProfileModel()
        )



}