package com.example.swift.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.usecase.home.CheckUserPlaceUseCase
import com.example.domain.usecase.home.FetchBoomBimListUseCase
import com.example.domain.usecase.home.FetchLessBoomBimListUseCase
import com.example.domain.usecase.home.FetchRegionUseCase
import com.example.domain.usecase.home.GetBoomBimUseCase
import com.example.domain.usecase.home.GetLessBoomBimUseCase
import com.example.domain.usecase.home.GetRegionUseCase
import com.example.domain.usecase.home.MakeAutoMessageUseCase
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
    private val fetchBoomBimListUseCase: FetchBoomBimListUseCase,
    private val fetchLessBoomBimListUseCase: FetchLessBoomBimListUseCase,
    getRegionUseCase: GetRegionUseCase,
    getLessBoomBimUseCase: GetLessBoomBimUseCase,
    getBoomBimUseCase: GetBoomBimUseCase,
    private val checkUserPlaceUseCase: CheckUserPlaceUseCase,
    private val makeAutoMessageUseCase: MakeAutoMessageUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            fetchRegionUseCase(getTodayDate())
            fetchBoomBimListUseCase()
        }
    }

    fun fetchLessBoomBimList(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            fetchLessBoomBimListUseCase(latitude, longitude)
        }
    }

    val region = getRegionUseCase()
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val lessBoomBimList = getLessBoomBimUseCase()
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val boomBimList = getBoomBimUseCase()
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
        address: String,
        onSuccess: (id: Int) -> Unit,
        onFailure: (msg:String) -> Unit
    ) {
        viewModelScope.launch {
            checkUserPlaceUseCase(uuid, name, address, latitude, longitude).collect{ result ->
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

    fun makeAutoMessage(
        memberPlaceName: String,
        congestionLevelName: String,
        congestionMessage: String,
        onSuccess: (msg: String) -> Unit,
        onFailure: () -> Unit
    ){
       viewModelScope.launch {
           makeAutoMessageUseCase(memberPlaceName, congestionLevelName, congestionMessage).collect { result ->
               when(result) {
                   is ApiResult.Success -> {
                       val msg = result.data.data.generatedCongestionMessage
                       onSuccess(msg)
                   }
                   is ApiResult.Fail.Error -> {
                       onFailure()
                   }
                   else -> {
                       onFailure()
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