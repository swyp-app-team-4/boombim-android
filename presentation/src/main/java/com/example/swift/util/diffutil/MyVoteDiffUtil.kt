package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.MyVoteItem
import com.example.domain.model.NotificationModel

object MyVoteDiffUtil : DiffUtil.ItemCallback<MyVoteItem>() {
    override fun areItemsTheSame(oldItem: MyVoteItem, newItem: MyVoteItem): Boolean {
        return oldItem.voteId == newItem.voteId
    }

    override fun areContentsTheSame(oldItem: MyVoteItem, newItem: MyVoteItem): Boolean {
        return oldItem == newItem
    }
}