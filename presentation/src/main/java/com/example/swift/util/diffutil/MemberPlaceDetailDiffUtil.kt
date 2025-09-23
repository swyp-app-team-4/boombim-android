package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.MemberCongestionItem
import com.example.domain.model.RegionResponse

object MemberPlaceDetailDiffUtil : DiffUtil.ItemCallback<MemberCongestionItem>() {
    override fun areItemsTheSame(oldItem: MemberCongestionItem, newItem: MemberCongestionItem): Boolean {
        return oldItem.memberCongestionId == newItem.memberCongestionId
    }

    override fun areContentsTheSame(oldItem: MemberCongestionItem, newItem: MemberCongestionItem): Boolean {
        return oldItem == newItem
    }
}