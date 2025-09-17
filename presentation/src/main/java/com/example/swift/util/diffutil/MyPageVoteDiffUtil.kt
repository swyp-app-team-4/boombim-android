package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.MemberCongestionItem
import com.example.domain.model.PopularityDetail

object MyPageVoteDiffUtil : DiffUtil.ItemCallback<PopularityDetail>() {
    override fun areItemsTheSame(oldItem: PopularityDetail, newItem: PopularityDetail): Boolean {
        return oldItem.voteId == newItem.voteId
    }

    override fun areContentsTheSame(oldItem: PopularityDetail, newItem: PopularityDetail): Boolean {
        return oldItem == newItem
    }
}