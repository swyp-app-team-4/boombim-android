package com.example.domain.usecase.home

import com.example.domain.model.ApiResult
import com.example.domain.model.CheckUserPlaceResponse
import com.example.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckUserPlaceUseCase @Inject constructor(
    private val homeRepository: HomeRepository
){
    suspend operator fun invoke(
        uuid: String,
        name: String,
        latitude: Double,
        longitude: Double
    ) : Flow<ApiResult<CheckUserPlaceResponse>> {
        return homeRepository.checkUserPlace(uuid, name, latitude, longitude)
    }
}