package com.example.domain.usecase.mypage

import javax.inject.Inject

class GetMyActivityUseCase @Inject constructor(
    private val myPageRepository: com.example.domain.repository.MyPageRepository
) {
    operator fun invoke() = myPageRepository.getMyActivityList()
}