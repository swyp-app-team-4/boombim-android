package com.example.swift.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    getRegionUseCase: GetRegionUseCase
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodayDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.now().format(formatter)
    }
}