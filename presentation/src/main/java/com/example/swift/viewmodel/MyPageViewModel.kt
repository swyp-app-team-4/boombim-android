package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ActionResult
import com.example.domain.model.ProfileModel
import com.example.domain.usecase.mypage.FetchMyActivityUseCase
import com.example.domain.usecase.mypage.FetchMyProfileUseCase
import com.example.domain.usecase.mypage.GetMyActivityUseCase
import com.example.domain.usecase.mypage.GetMyProfileUseCase
import com.example.domain.usecase.mypage.PatchProfileImageUseCase
import com.example.domain.usecase.mypage.PatchUserNickNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val fetchMyProfileUseCase: FetchMyProfileUseCase,
    getMyProfileUseCase: GetMyProfileUseCase,
    private val patchUserNickNameUseCase: PatchUserNickNameUseCase,
    private val patchProfileImageUseCase: PatchProfileImageUseCase,
    private val fetchMyActivityUseCase: FetchMyActivityUseCase,
    getMyActivityUseCase: GetMyActivityUseCase

): ViewModel(){

    init {
        viewModelScope.launch {
            fetchMyProfileUseCase()
            fetchMyActivityUseCase()
        }
    }

    val profile = getMyProfileUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProfileModel()
        )

    val activity = getMyActivityUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun refreshProfile() {
        viewModelScope.launch {
            fetchMyProfileUseCase()
        }
    }

    fun patchNickName(
        name: String,
        onSuccess: () -> Unit,
        onFail: (msg: String) -> Unit
    ){
        viewModelScope.launch {
            when(val result = patchUserNickNameUseCase(name)){
                is ActionResult.Fail -> {
                    onFail(result.msg)
                }
                is ActionResult.Success -> {
                    onSuccess()
                }
            }
        }
    }

    fun patchProfileImage(
        imagePath: String
    ){
        viewModelScope.launch {
           patchProfileImageUseCase(imagePath)
        }
    }



}