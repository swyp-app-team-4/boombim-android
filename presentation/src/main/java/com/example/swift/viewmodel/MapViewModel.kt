package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.map.FetchOfficialPlaceUseCase
import com.example.domain.usecase.map.FetchViewPortPlaceList
import com.example.domain.usecase.map.GetOfficialPlaceUseCase
import com.example.domain.usecase.map.GetViewPortPlaceList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    getViewPortPlaceList: GetViewPortPlaceList,
    getOfficialPlaceUseCase: GetOfficialPlaceUseCase,
    private val fetchViewPortPlaceList: FetchViewPortPlaceList,
    private val fetchOfficialPlaceUseCase: FetchOfficialPlaceUseCase
): ViewModel() {

    val viewPortPlaceList = getViewPortPlaceList()
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

    fun fetchViewPortList(
        topLeftLongitude: Double,
        topLeftLatitude: Double,
        bottomRightLongitude: Double,
        bottomRightLatitude: Double,
        memberLongitude: Double,
        memberLatitude: Double
    ) {
        viewModelScope.launch {
            fetchViewPortPlaceList(
                topLeftLongitude = topLeftLongitude,
                topLeftLatitude = topLeftLatitude,
                bottomRightLongitude = bottomRightLongitude,
                bottomRightLatitude = bottomRightLatitude,
                memberLongitude = memberLongitude,
                memberLatitude = memberLatitude
            )
        }
    }

    fun fetchOfficial(placeId: Int){
       viewModelScope.launch {
           fetchOfficialPlaceUseCase(placeId)
       }
    }

}