package com.example.swift.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

object DateTimeUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true) // 소수점 이하 0~9자리 허용
        .optionalEnd()
        .toFormatter()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeAgo(createdAt: String): String {
        val createdTime = LocalDateTime.parse(createdAt, formatter)
        val now = LocalDateTime.now(ZoneId.systemDefault())

        val duration = Duration.between(createdTime, now)
        val minutes = duration.toMinutes()
        val hours = duration.toHours()
        val days = duration.toDays()

        return when {
            minutes < 1 -> "방금전"
            minutes < 60 -> "${minutes}분 전"
            hours < 24 -> "${hours}시간 전"
            days < 30 -> "${days}일 전"
            else -> createdAt.substring(0, 10)
        }
    }
}
