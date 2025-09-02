package com.example.domain.usecase.mypage

import com.example.domain.repository.MyPageRepository
import javax.inject.Inject

class PatchProfileImageUseCase @Inject  constructor(
    private val myPageRepository: MyPageRepository
){
    suspend operator fun invoke(imagePath: String) =
        myPageRepository.patchProfileImage(
            imagePath = imagePath
        )
}