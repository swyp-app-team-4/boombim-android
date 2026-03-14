package com.example.domain.usecase.mypage

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.model.ActionResult
import com.example.domain.model.MakeCongestionResponse
import com.example.domain.model.MyActivityResponse
import com.example.domain.repository.HomeRepository
import com.example.domain.repository.MyPageRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
@RequiresApi(Build.VERSION_CODES.O)
class RegisterCongestionUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    operator fun invoke(
        posName: String,
        congestionLevel: String
    ) {
        val now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

        myPageRepository.addMyActivity(
            posName = posName,
            congestionLevel = congestionLevel,
            createdAt = now
        )
    }
}
