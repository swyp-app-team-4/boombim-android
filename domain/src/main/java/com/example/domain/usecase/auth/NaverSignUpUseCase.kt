package com.example.domain.usecase.auth

import com.example.domain.model.ApiResult
import com.example.domain.model.SocialLoginSignUpResult
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class NaverSignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        provider: String,
        accessToken: String,
        refreshToken: String,
        expiresIn: Int,
        idToken: String
    ) : ApiResult<SocialLoginSignUpResult> {
        return authRepository.socialLogin(
            provider = provider,
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn,
            idToken = idToken
        )
    }
}