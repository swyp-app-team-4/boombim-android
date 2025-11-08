package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.PointHistory

object PointHistoryDiffUtil : DiffUtil.ItemCallback<PointHistory>() {
    override fun areItemsTheSame(oldItem: PointHistory, newItem: PointHistory): Boolean {
        return oldItem.pointHistoryId == newItem.pointHistoryId
    }

    override fun areContentsTheSame(oldItem: PointHistory, newItem: PointHistory): Boolean {
        return oldItem == newItem
    }
}