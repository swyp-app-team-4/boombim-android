package com.example.swift.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datastore.AppManageDataStore
import com.example.domain.model.TokenStatus
import com.example.domain.provider.TokenProvider
import com.example.domain.usecase.auth.CheckUserTokenExpiredUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appManageDataStore: AppManageDataStore,
    private val checkUserTokenExpiredUseCase: CheckUserTokenExpiredUseCase,
    private val tokenProvider: TokenProvider
) : ViewModel() {
    fun checkSplash(
        complete: (dest: String) -> Unit,
    ){
        viewModelScope.launch {
            Log.d("SplashVM", "refreshToken=${tokenProvider.getRefreshToken()}")
            Log.d("SplashVM", "isFirstLaunch=${appManageDataStore.getIsFirstLaunch().first()}")
            val timeJob = async {
                //3초대기
                delay(1000L)
            }
            val checkLogin = async {
                checkUserTokenExpiredUseCase()
            }
            val isFirstLaunch = async {
                //최초 첫 실행 인지 확인
                appManageDataStore.getIsFirstLaunch().first()
            }
            joinAll(timeJob, checkLogin, isFirstLaunch)

            val result =checkLogin.await()
            val isFirst = isFirstLaunch.await()
            val dest =if(result == TokenStatus.NOT_EXPIRED){
                "홈프래그먼트"
            } else{
                if(isFirst){
                    "온보딩엑티비티"
                }else{
                    "소셜로그인프래그먼트"
                }
            }
            complete(dest)
        }
    }
}