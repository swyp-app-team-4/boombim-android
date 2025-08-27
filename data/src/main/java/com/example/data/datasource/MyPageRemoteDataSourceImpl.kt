package com.example.data.datasource

import com.example.data.network.auth.request.RefreshTokenRequest
import com.example.data.network.mypage.MyPageApi
import com.example.data.network.mypage.request.PatchNickNameRequest
import com.example.data.network.safeFlow
import com.example.domain.datasource.MyPageRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    override suspend fun patchNickName(name: String): ApiResult<*> {
        val request = PatchNickNameRequest(name = name)
        return safeFlow { myPageApi.patchNickName(request)}.first()
    }

    override suspend fun logout(refresh: String): ApiResult<*> {
        val request = RefreshTokenRequest(refresh)
        return safeFlow { myPageApi.logout(request)}.first()
    }

}