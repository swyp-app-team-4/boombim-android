package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.map.FetchMemberPlaceDetailUseCase
import com.example.domain.usecase.map.FetchMemberPlaceUseCase
import com.example.domain.usecase.map.FetchOfficialPlaceUseCase
import com.example.domain.usecase.map.FetchViewPortPlaceList
import com.example.domain.usecase.map.GetMemberPlaceDetailUseCase
import com.example.domain.usecase.map.GetMemberPlaceUseCase
import com.example.domain.usecase.map.GetOfficialPlaceUseCase
import com.example.domain.usecase.map.GetViewPortPlaceList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    getViewPortPlaceList: GetViewPortPlaceList,
    getOfficialPlaceUseCase: GetOfficialPlaceUseCase,
    getMemberPlaceUseCase: GetMemberPlaceUseCase,
    private val fetchMemberPlaceUseCase: FetchMemberPlaceUseCase,
    private val fetchViewPortPlaceList: FetchViewPortPlaceList,
    private val fetchOfficialPlaceUseCase: FetchOfficialPlaceUseCase,
    private val fetchMemberPlaceDetailUseCase: FetchMemberPlaceDetailUseCase,
    getMemberPlaceDetailUseCase: GetMemberPlaceDetailUseCase
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    val viewPortPlaceList = getViewPortPlaceList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val memberPlaceList = getMemberPlaceUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val officialPlace = getOfficialPlaceUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            null
        )

    val memberPlaceDetailList = getMemberPlaceDetailUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    // ------------------------
    //   ★ 로딩 처리 추가
    // ------------------------
    private suspend fun <T> runWithLoading(block: suspend () -> T): T {
        _isLoading.value = true
        return try {
            block()
        } finally {
            _isLoading.value = false
        }
    }

    fun fetchOfficial(placeId: Int) {
        viewModelScope.launch {
            runWithLoading {
                fetchOfficialPlaceUseCase(placeId)
            }
        }
    }

    fun fetchMemberPlaceList(officialPlaceId: Int){
        viewModelScope.launch {
            runWithLoading {
                fetchMemberPlaceDetailUseCase(officialPlaceId)
            }
        }
    }

    fun fetchViewPortList(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double,
        zoomLevel: Int
    ) {
        viewModelScope.launch {
            runWithLoading {
                fetchViewPortPlaceList(
                    topLeftLongitude,
                    topLeftLatitude,
                    bottomRightLongitude,
                    bottomRightLatitude,
                    memberLongitude,
                    memberLatitude,
                    zoomLevel
                )
            }
        }
    }

    fun fetchMemberPlaceList(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double,
        zoomLevel: Int
    ) {
        viewModelScope.launch {
            runWithLoading {
                fetchMemberPlaceUseCase(
                    topLeftLongitude,
                    topLeftLatitude,
                    bottomRightLongitude,
                    bottomRightLatitude,
                    memberLongitude,
                    memberLatitude,
                    zoomLevel
                )
            }
        }
    }
}
