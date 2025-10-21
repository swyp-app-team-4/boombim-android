package com.example.domain.model

sealed class MyActivityItem {
    data class DateHeader(val date: String) : MyActivityItem()
    data class ActivityItem(val data: MyActivityResponse) : MyActivityItem()
}
