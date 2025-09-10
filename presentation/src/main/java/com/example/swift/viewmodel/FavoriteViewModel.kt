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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val postFavoriteUseCase: PostFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    getFavoriteUseCase: GetFavoriteUseCase,
    private val fetchFavoriteUseCase: FetchFavoriteUseCase
) : ViewModel() {

   fun fetchFavorites() {
        viewModelScope.launch {
            fetchFavoriteUseCase()
        }
    }

    val favoriteList = getFavoriteUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )



    fun postFavorite(
        memberPlaceId: Int,
        placeType: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        viewModelScope.launch {
            val result = postFavoriteUseCase(memberPlaceId,placeType)
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
        placeType:String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        viewModelScope.launch {
            val result = deleteFavoriteUseCase(memberPlaceId,placeType)
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