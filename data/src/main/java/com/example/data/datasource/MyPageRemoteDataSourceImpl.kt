package com.example.data.datasource

import com.example.data.network.auth.request.RefreshTokenRequest
import com.example.data.network.mypage.MyPageApi
import com.example.data.network.mypage.request.DeleteAccountRequest
import com.example.data.network.mypage.request.PatchNickNameRequest
import com.example.data.network.safeFlow
import com.example.domain.datasource.MyPageRemoteDataSource
import com.example.domain.model.ApiResult
import com.example.domain.model.EventCampaign
import com.example.domain.model.MyActivityResponse
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.PatchProfileImageResponse
import com.example.domain.model.PointHistoryResponse
import com.example.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class MyPageRemoteDataSourceImpl @Inject constructor(
    private val myPageApi: MyPageApi
) : MyPageRemoteDataSource {

    override suspend fun getProfile(): Flow<ApiResult<ProfileModel>> {
       return safeFlow {
           myPageApi.getProfile()
       }
    }

    override suspend fun patchNickName(name: String): ApiResult<*> {
        val request = PatchNickNameRequest(name = name)
        return safeFlow { myPageApi.patchNickName(request)}.first()
    }

    override suspend fun logout(refresh: String): ApiResult<*> {
        val request = RefreshTokenRequest(refresh)
        return safeFlow { myPageApi.logout(request)}.first()
    }

    override suspend fun patchProfileImage(imagePath: String): ApiResult<PatchProfileImageResponse> {
        val file = File(imagePath)

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            name = "multipartFile",
            filename = file.name,
            body = requestFile
        )

        return safeFlow { myPageApi.patchProfileImage(multipartBody)}.first()
    }

    override suspend fun deleteUser(reason: String): ApiResult<*> {
        val request = DeleteAccountRequest(reason)
        return safeFlow { myPageApi.deleteAccount(request)}.first()
    }

    override suspend fun getMyActivity(): Flow<ApiResult<List<MyActivityResponse>>> {
        return safeFlow { myPageApi.getMyActivity() }
    }

    override suspend fun getEvent(): Flow<ApiResult<EventCampaign>> {
        return safeFlow {
            myPageApi.getEvent()
        }
    }

    override suspend fun getPointHistory(): Flow<ApiResult<PointHistoryResponse>> {
        return safeFlow {
            myPageApi.getMyPoint()
        }
    }

}