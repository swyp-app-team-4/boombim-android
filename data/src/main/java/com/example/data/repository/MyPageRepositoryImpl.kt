package com.example.data.repository

import com.example.data.extension.covertApiResultToActionResultIfSuccess
import com.example.data.extension.covertApiResultToActionResultIfSuccessEmpty
import com.example.data.network.mypage.MyPageApi
import com.example.domain.datasource.MyPageRemoteDataSource
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.EventCampaign
import com.example.domain.model.MyActivityResponse
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.PatchProfileImageResponse
import com.example.domain.model.PointHistory
import com.example.domain.model.PopularityDetail
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

    private var _myActivity = MutableStateFlow<List<MyActivityResponse>>(emptyList())
    private val myActivity
        get() = _myActivity.asStateFlow()

    private var _myPointList = MutableStateFlow<List<PointHistory>>(emptyList())
    private val myPointList
        get() = _myPointList.asStateFlow()

    private var _myPoint = MutableStateFlow(0)
    private val myPoint
        get() = _myPoint.asStateFlow()

    private var _myEventInfo = MutableStateFlow(EventCampaign())
    private val myEventInfo
        get() = _myEventInfo.asStateFlow()


    override fun getMyProfile(): Flow<ProfileModel> = myPage

    override fun getMyActivityList(): Flow<List<MyActivityResponse>> = myActivity

    override fun getMyPointList(): Flow<List<PointHistory>> = myPointList

    override fun getMyPointTotal(): Flow<Int> = myPoint

    override fun getEventCampaignInfo(): Flow<EventCampaign> = myEventInfo


    override suspend fun getProfile() {
        myPageRemoteDataSource.getProfile().first().let { result ->
            if (result is ApiResult.Success){
                _myPage.update {
                    result.data
                }
            }
        }
    }

    override suspend fun getMyActivity() {
       myPageRemoteDataSource.getMyActivity().first().let { result ->
              if(result is ApiResult.Success){
                _myActivity.update {
                     result.data
                }
              }
       }
    }

    override suspend fun getMyPoint() {
       myPageRemoteDataSource.getPointHistory().first().let { result ->
           if(result is ApiResult.Success){
               _myPointList.update {
                   result.data.getPointHistoryRes
               }
               _myPoint.update {
                   result.data.point
               }
           }
       }
    }

    override suspend fun getEventCampaign() {
        myPageRemoteDataSource.getEvent().first().let { result ->
            if(result is ApiResult.Success){
                _myEventInfo.update {
                    result.data
                }
            }
        }
    }

    override suspend fun patchNickName(name: String): ActionResult<*> {
        val result = myPageRemoteDataSource.patchNickName(name)
        if(result is ApiResult.SuccessEmpty){
            _myPage.update { profile ->
                profile.copy(
                    name = name
                )
            }
        }
        return result.covertApiResultToActionResultIfSuccessEmpty()
    }

    override suspend fun logout(refresh: String): ActionResult<*> {
        val result = myPageRemoteDataSource.logout(refresh)
        return result.covertApiResultToActionResultIfSuccessEmpty()
    }

    override suspend fun patchProfileImage(imagePath: String): ActionResult<PatchProfileImageResponse> {
        val result = myPageRemoteDataSource.patchProfileImage(imagePath)
        return result.covertApiResultToActionResultIfSuccess()
    }

    override suspend fun deleteMember(reason: String): ActionResult<*> {
        val result = myPageRemoteDataSource.deleteUser(reason)
        return result.covertApiResultToActionResultIfSuccessEmpty()
    }
}