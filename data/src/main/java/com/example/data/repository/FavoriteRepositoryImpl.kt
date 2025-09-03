package com.example.data.repository

import com.example.data.extension.covertApiResultToActionResultIfSuccess
import com.example.domain.datasource.FavoriteRemoteDatasource
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.FavoriteData
import com.example.domain.model.NotificationModel
import com.example.domain.model.PostFavoriteResponse
import com.example.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteRemoteDatasource: FavoriteRemoteDatasource
) : FavoriteRepository{

    private val _favoriteList = MutableStateFlow(emptyList<FavoriteData>())

    private val favoriteList
        get() = _favoriteList.asStateFlow()

    override suspend fun getFavoriteList(): Flow<List<FavoriteData>> = favoriteList

    override suspend fun postFavorite(memberPlaceId: Int): ActionResult<PostFavoriteResponse> {
        val result =  favoriteRemoteDatasource.postFavorite(memberPlaceId)

        return result.covertApiResultToActionResultIfSuccess()
    }

    override suspend fun deleteFavorite(memberPlaceId: Int): ActionResult<PostFavoriteResponse> {
        val result = favoriteRemoteDatasource.deleteFavorite(memberPlaceId)
        val actionResult = result.covertApiResultToActionResultIfSuccess()

        if (actionResult is ActionResult.Success) {
            _favoriteList.update { list ->
                list.filter { it.memberPlaceId != memberPlaceId }
            }
        }

        return actionResult
    }


    override suspend fun getFavorites() {
        favoriteRemoteDatasource.getFavorites().first().let { result ->
            if (result is ApiResult.Success){
                _favoriteList.update {
                    result.data.data
                }
            }
        }
    }


}