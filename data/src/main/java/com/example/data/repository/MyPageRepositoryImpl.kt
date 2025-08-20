package com.example.data.repository

import com.example.data.network.mypage.MyPageApi
import com.example.domain.datasource.MyPageRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.ProfileModel
import com.example.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageRemoteDataSource: MyPageRemoteDataSource
) : MyPageRepository {

    private var _myPage: MutableStateFlow<ProfileModel> = MutableStateFlow(ProfileModel())
    private val myPage
        get() = _myPage.asStateFlow()

    override fun getMyProfile(): Flow<ProfileModel> = myPage


    override suspend fun getProfile() {
        myPageRemoteDataSource.getProfile().first().let { result ->
            if (result is ApiResult.Success){
                _myPage.update {
                    result.data
                }
            }
        }
    }
}