package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ActionResult
import com.example.domain.usecase.home.MakeCongestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeCongestionViewModel @Inject constructor(
    private val makeCongestionUseCase: MakeCongestionUseCase
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
}