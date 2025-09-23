package com.example.domain.usecase.notification

import com.example.domain.model.ActionResult
import com.example.domain.provider.FcmTokenProvider
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateFcmToken @Inject constructor(
    private val authRepository: AuthRepository,
    private val fcmTokenProvider: FcmTokenProvider
) {
    suspend operator fun invoke(token: String): ActionResult<Unit>{
        val savedAccessToken = fcmTokenProvider.getSavedFcmToken()
        if(savedAccessToken != token){
           val result = authRepository.saveFcmToken(token,"ANDROID")
            if(result is ActionResult.Success){
                fcmTokenProvider.setSavedFcmToken(token)
            }
            return ActionResult.Success(Unit)
        }
        return ActionResult.Fail("")
    }
}