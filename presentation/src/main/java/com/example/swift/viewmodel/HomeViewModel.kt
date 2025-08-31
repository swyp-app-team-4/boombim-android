package com.example.swift.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ApiResult
import com.example.domain.usecase.home.CheckUserPlaceUseCase
import com.example.domain.usecase.home.FetchRegionUseCase
import com.example.domain.usecase.home.GetRegionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchRegionUseCase: FetchRegionUseCase,
    getRegionUseCase: GetRegionUseCase,
    private val checkUserPlaceUseCase: CheckUserPlaceUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            fetchRegionUseCase(getTodayDate())
        }
    }

    val region = getRegionUseCase()
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    fun checkUserPlace(
        uuid: String,
        name: String,
        latitude: Double,
        longitude: Double,
        onSuccess: (id: Int) -> Unit,
        onFailure: (msg:String) -> Unit
    ) {
        viewModelScope.launch {
            checkUserPlaceUseCase(uuid, name, latitude, longitude).collect{ result ->
                when(result) {
                    is ApiResult.Success -> {
                        val id = result.data.data.memberPlaceId
                        onSuccess(id)
                    }
                    is ApiResult.Fail.Error -> {
                        onFailure(result.message ?: "장소확인 실패")
                    }
                    else -> {
                        onFailure("오류 발생")
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodayDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.now().format(formatter)
    }
}