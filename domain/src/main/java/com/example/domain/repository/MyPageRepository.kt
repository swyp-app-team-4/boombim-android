package com.example.domain.repository

import com.example.domain.model.ActionResult
import com.example.domain.model.MyActivityResponse
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.NotificationModel
import com.example.domain.model.PatchProfileImageResponse
import com.example.domain.model.PopularityDetail
import com.example.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {

    fun getMyProfile(): Flow<ProfileModel>

    fun getMyAnswerList(): Flow<List<MyPageVoteResponse>>

    fun getMyQuestionList(): Flow<List<MyPageVoteResponse>>

    fun getMyActivityList(): Flow<List<MyActivityResponse>>

    suspend fun getProfile()

    suspend fun getMyAnswer()

    suspend fun getMyQuestion()

    suspend fun getMyActivity()

    suspend fun patchNickName(name: String) :ActionResult<*>

    suspend fun logout(refresh: String) : ActionResult<*>

    suspend fun patchProfileImage(imagePath: String): ActionResult<PatchProfileImageResponse>

    suspend fun deleteMember(reason: String): ActionResult<*>
}