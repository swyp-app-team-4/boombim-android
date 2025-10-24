package com.example.domain.usecase.point

import com.example.domain.repository.MyPageRepository
import javax.inject.Inject

class FetchPointListUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(){
        myPageRepository.getMyPoint()
    }
}