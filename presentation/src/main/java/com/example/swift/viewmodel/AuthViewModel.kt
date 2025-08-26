package com.example.swift.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.data.datastore.AppManageDataStore
import com.example.domain.usecase.NaverSignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val naverSignUpUseCase: NaverSignUpUseCase,
    private val appManageDataStore: AppManageDataStore
) :ViewModel() {

    fun socialLogin(
        accessToken: String,
        refreshToken: String,
        provider: String,
        onSuccess: (nameFlag: Boolean) -> Unit,
        onFail: (msg: String) -> Unit
    ){
        viewModelScope.launch {
            appManageDataStore.saveKakaoAccessToken(accessToken)
            appManageDataStore.saveKakaoRefreshToken(refreshToken)
        }

        viewModelScope.launch {
            when(val result = naverSignUpUseCase(
                provider = provider,
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresIn = 3600,
                idToken = ""
            )) {
                is ActionResult.Fail -> {
                    onFail(result.msg)
                }
                is ActionResult.Success -> {
                    onSuccess(result.data.nameFlag)
                }
            }
        }
    }
}