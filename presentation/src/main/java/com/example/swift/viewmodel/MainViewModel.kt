package com.example.swift.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.UpdateFcmToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val updateFcmToken: UpdateFcmToken
) : ViewModel() {

    fun updateToken(token: String) {
        viewModelScope.launch {
            updateFcmToken(token)
        }
    }

}