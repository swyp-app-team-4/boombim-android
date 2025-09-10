package com.example.swift.view.main.home.notification.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemEventNotificationBinding
import com.example.domain.model.NotificationModel
import com.example.swift.util.DateTimeUtils

class EventNotificationAdapter(private val items: List<NotificationModel>) :
    RecyclerView.Adapter<EventNotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private val binding: ItemEventNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: NotificationModel) {
            binding.textContent.text = item.title
            binding.textTime.text = DateTimeUtils.getTimeAgo(item.alarmTime)

            val backgroundColorRes = if (item.deliveryStatus == "읽음") {
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
