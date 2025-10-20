package com.example.domain.usecase.mypage

import javax.inject.Inject

class FetchMyActivityUseCase @Inject constructor(
    private val myPageRepository: com.example.domain.repository.MyPageRepository
) {
    suspend operator fun invoke(){
        myPageRepository.getMyActivity()
    }
}