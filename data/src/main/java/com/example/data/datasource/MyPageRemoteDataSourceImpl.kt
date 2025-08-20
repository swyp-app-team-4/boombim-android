package com.example.data.datasource

import com.example.data.network.mypage.MyPageApi
import com.example.data.network.safeFlow
import com.example.domain.datasource.MyPageRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyPageRemoteDataSourceImpl @Inject constructor(
    private val myPageApi: MyPageApi
) : MyPageRemoteDataSource {

    override suspend fun getProfile(): Flow<ApiResult<ProfileModel>> {
       return safeFlow {
           myPageApi.getProfile()
       }
    }

    override suspend fun getMyAnswer(): Flow<ApiResult<List<MyPageVoteResponse>>> {
        return safeFlow { myPageApi.getMyAnswer() }
    }

    override suspend fun getMyQuestion(): Flow<ApiResult<List<MyPageVoteResponse>>> {
       return safeFlow { myPageApi.getMyQuestion() }
    }

}