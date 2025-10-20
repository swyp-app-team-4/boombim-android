package com.example.swift.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.model.MyActivityItem
import com.example.domain.model.MyActivityResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun groupActivitiesByDate(list: List<MyActivityResponse>): List<MyActivityItem> {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd", Locale.KOREA)

    return list
        .groupBy { it.createdAt.substring(0, 10) }
        .toSortedMap(compareByDescending { it })
        .flatMap { (dateStr, activities) ->
            val localDate = LocalDate.parse(dateStr, inputFormatter)
            val dayOfWeekKorean = when (localDate.dayOfWeek.value) {
                1 -> "월"
                2 -> "화"
                3 -> "수"
                4 -> "목"
                5 -> "금"
                6 -> "토"
                7 -> "일"
                else -> ""
            }

            val headerText = "${localDate.format(outputFormatter)} ($dayOfWeekKorean)"
            listOf(MyActivityItem.DateHeader(headerText)) +
                    activities.map { MyActivityItem.ActivityItem(it) }
        }
}