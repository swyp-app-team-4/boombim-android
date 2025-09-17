package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.MemberCongestionItem
import com.example.domain.model.MyPageVoteResponse

object MyPageVoteSectionDiffUtil : DiffUtil.ItemCallback<MyPageVoteResponse>() {
    override fun areItemsTheSame(oldItem: MyPageVoteResponse, newItem: MyPageVoteResponse): Boolean {
        return oldItem.tempId == newItem.tempId
    }

    override fun areContentsTheSame(oldItem: MyPageVoteResponse, newItem: MyPageVoteResponse): Boolean {
        return oldItem == newItem
    }
}