package com.example.swift.view.main.mypage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemEventNotificationBinding
import com.boombim.android.databinding.ItemMyPageVoteBinding
import com.example.domain.model.NotificationModel
import com.example.domain.model.PopularityDetail
import com.example.swift.view.main.home.notification.adapter.EventNotificationAdapter

class MyPageVoteAdapter (private val items: List<PopularityDetail>) :
    RecyclerView.Adapter<MyPageVoteAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private val binding: ItemMyPageVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: PopularityDetail) {
            binding.placeName.text = item.posName
            binding.textStatus.text = item.popularStatus
            binding.textPeopleNumber.text = "${item.popularCnt}명"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyPageVoteBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

}