package com.example.domain.usecase.mypage

import com.example.domain.repository.MyPageRepository
import javax.inject.Inject

class PatchUserNickNameUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(name: String) =
        myPageRepository.patchNickName(
            name = name
        )
}