package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.usecase.favorite.PostFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val postFavoriteUseCase: PostFavoriteUseCase
) : ViewModel() {

    fun postFavorite(
        memberPlaceId: Int,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        viewModelScope.launch {
            val result = postFavoriteUseCase(memberPlaceId)
            when(result) {
                is ActionResult.Success -> {
                    onSuccess()
                }
                is ActionResult.Fail -> {
                    onFail()
                }
            }

        }
    }
}