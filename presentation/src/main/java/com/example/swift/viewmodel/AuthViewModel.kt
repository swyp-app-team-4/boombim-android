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
                is ApiResult.Success -> onSuccess(result.data.nameFlag)
                is ApiResult.Fail.Error -> {
                    val message = when (result.code) {
                        409 -> "이미 사용중인 이메일입니다"
                        422 -> "유효하지 않은 액세스 토큰입니다"
                        else -> result.message ?: "로그인 실패"
                    }
                    onFail(message)
                }
                is ApiResult.Fail.Exception -> onFail("예상치 못한 오류")
                is ApiResult.SuccessEmpty ->  ""
            }
        }
    }
}