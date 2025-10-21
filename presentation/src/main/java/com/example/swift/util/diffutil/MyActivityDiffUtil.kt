package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.MemberCongestionItem
import com.example.domain.model.MyActivityItem
import com.example.domain.model.MyActivityResponse

object MyActivityDiffUtil : DiffUtil.ItemCallback<MyActivityItem>() {
    override fun areItemsTheSame(oldItem: MyActivityItem, newItem: MyActivityItem): Boolean {
        return when {
            oldItem is MyActivityItem.ActivityItem && newItem is MyActivityItem.ActivityItem ->
                oldItem.data.id == newItem.data.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: MyActivityItem, newItem: MyActivityItem): Boolean {
        return oldItem == newItem
    }
}
