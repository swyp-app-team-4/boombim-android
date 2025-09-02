package com.example.data.repository

import com.example.data.extension.covertApiResultToActionResultIfSuccess
import com.example.domain.datasource.FavoriteRemoteDatasource
import com.example.domain.model.ActionResult
import com.example.domain.model.PostFavoriteResponse
import com.example.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteRemoteDatasource: FavoriteRemoteDatasource
) : FavoriteRepository{

    override suspend fun postFavorite(memberPlaceId: Int): ActionResult<PostFavoriteResponse> {

        val result =  favoriteRemoteDatasource.postFavorite(memberPlaceId)
        return result.covertApiResultToActionResultIfSuccess()
    }


}