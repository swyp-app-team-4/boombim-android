package com.example.swift.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ActionResult
import com.example.domain.usecase.home.MakeCongestionUseCase
import com.example.domain.usecase.mypage.RegisterCongestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeCongestionViewModel @Inject constructor(
    private val makeCongestionUseCase: MakeCongestionUseCase,
    val registerCongestionUseCase: RegisterCongestionUseCase
) : ViewModel(){

    fun makeCongestion(
        memberPlaceId: Int,
        congestionLevelId: Int,
        congestionMessage: String,
        latitude: Double,
        longitude: Double,
        onSuccess: (msg: String) -> Unit,
        onFailure: (msg:String) -> Unit
    ) {
      viewModelScope.launch {
          val result = makeCongestionUseCase(memberPlaceId, congestionLevelId, congestionMessage, latitude, longitude)
            when(result) {
                is ActionResult.Success -> {
                    onSuccess("혼잡도 등록 성공")
                }
                is ActionResult.Fail -> {
                    onFailure(result.msg ?: "혼잡도 알리기 실패")
                }
            }
      }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMyActivity(posName: String, congestionLevel: Int) {

        val levelText = when (congestionLevel) {
            1 -> "여유"
            2 -> "보통"
            3 -> "약간 붐빔"
            4 -> "붐빔"
            else -> "알 수 없음"
        }

        registerCongestionUseCase(
            posName = posName,
            congestionLevel = levelText
        )
    }

}