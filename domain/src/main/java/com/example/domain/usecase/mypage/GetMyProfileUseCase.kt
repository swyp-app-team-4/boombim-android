package com.example.domain.usecase.mypage

import com.example.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    operator fun invoke() = myPageRepository.getMyProfile()
}