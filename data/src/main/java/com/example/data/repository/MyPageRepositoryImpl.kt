package com.example.data.repository

import com.example.data.extension.covertApiResultToActionResultIfSuccessEmpty
import com.example.data.network.mypage.MyPageApi
import com.example.domain.datasource.MyPageRemoteDataSource
import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult
import com.example.domain.model.MyPageVoteResponse
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

    private var _myAnswer = MutableStateFlow<List<MyPageVoteResponse>>(emptyList())
    private val myAnswer = _myAnswer.asStateFlow()

    private var _myQuestion = MutableStateFlow<List<MyPageVoteResponse>>(emptyList())
    private val myQuestion = _myQuestion.asStateFlow()


    override fun getMyProfile(): Flow<ProfileModel> = myPage

    override fun getMyAnswerList(): Flow<List<MyPageVoteResponse>> = myAnswer

    override fun getMyQuestionList(): Flow<List<MyPageVoteResponse>> = myQuestion



    override suspend fun getProfile() {
        myPageRemoteDataSource.getProfile().first().let { result ->
            if (result is ApiResult.Success){
                _myPage.update {
                    result.data
                }
            }
        }
    }

    override suspend fun getMyAnswer() {
        myPageRemoteDataSource.getMyAnswer().first().let{ result ->
            if(result is ApiResult.Success){
                _myAnswer.update {
                    result.data
                }
            }
        }
    }

    override suspend fun getMyQuestion() {
        myPageRemoteDataSource.getMyQuestion().first().let{ result ->
            if(result is ApiResult.Success){
                _myQuestion.update {
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
}