package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

class PatchAlarmUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke() = notificationRepository.patchAlarm()
}