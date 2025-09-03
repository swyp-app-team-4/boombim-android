package com.example.swift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.usecase.favorite.DeleteFavoriteUseCase
import com.example.domain.usecase.favorite.FetchFavoriteUseCase
import com.example.domain.usecase.favorite.GetFavoriteUseCase
import com.example.domain.usecase.favorite.PostFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val postFavoriteUseCase: PostFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    getFavoriteUseCase: GetFavoriteUseCase,
    private val fetchFavoriteUseCase: FetchFavoriteUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            fetchFavoriteUseCase()
        }
    }



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

    fun deleteFavorite(
        memberPlaceId: Int,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        viewModelScope.launch {
            val result = deleteFavoriteUseCase(memberPlaceId)
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