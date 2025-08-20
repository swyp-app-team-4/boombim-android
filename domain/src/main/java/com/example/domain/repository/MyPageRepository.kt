package com.example.domain.repository

import com.example.domain.model.NotificationModel
import com.example.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {

    fun getMyProfile(): Flow<ProfileModel>

    suspend fun getProfile()
}