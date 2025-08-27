package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datastore.AppManageDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val appManageDataStore: AppManageDataStore
) :ViewModel() {

    fun updateNotFirstLaunch() {
        viewModelScope.launch {
            appManageDataStore.setNotFirstLaunch()
        }
    }
}