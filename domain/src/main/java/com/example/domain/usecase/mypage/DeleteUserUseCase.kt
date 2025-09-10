package com.example.domain.usecase.mypage

import com.example.domain.repository.MyPageRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(reason: String) =
        myPageRepository.deleteMember(reason)
}