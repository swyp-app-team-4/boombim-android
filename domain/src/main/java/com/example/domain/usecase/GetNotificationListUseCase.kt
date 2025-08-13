package com.example.domain.usecase

import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationListUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke() = notificationRepository.getNotificationList()
}