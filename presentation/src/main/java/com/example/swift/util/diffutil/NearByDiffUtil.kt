package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.CongestionData
import com.example.domain.model.MemberCongestionItem

object NearByDiffUtil : DiffUtil.ItemCallback<CongestionData>() {
    override fun areItemsTheSame(oldItem: CongestionData, newItem: CongestionData): Boolean {
        return oldItem.officialPlaceId == newItem.officialPlaceId
    }

    override fun areContentsTheSame(oldItem: CongestionData, newItem: CongestionData): Boolean {
        return oldItem == newItem
    }
}