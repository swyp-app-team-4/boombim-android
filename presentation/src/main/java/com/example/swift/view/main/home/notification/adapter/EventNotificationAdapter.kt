package com.example.swift.view.main.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemEventNotificationBinding
import com.example.domain.model.NotificationModel

class EventNotificationAdapter(private val items: List<NotificationModel>) :
    RecyclerView.Adapter<EventNotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private val binding: ItemEventNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationModel) {
            binding.textContent.text = item.content
            binding.textTime.text = item.time

            val backgroundColorRes = if (item.isRead) {
                R.color.gray_scale_1
            } else {
                R.color.gray_scale_2
            }

            binding.rootLayout.setBackgroundResource(backgroundColorRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventNotificationBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

}
