package com.example.domain.usecase

import com.example.domain.repository.MyPageRepository
import javax.inject.Inject

class FetchMyProfileUseCase  @Inject constructor(
    private val myPageRepository: MyPageRepository
){
    suspend operator fun invoke(){
        myPageRepository.getProfile()
    }
}