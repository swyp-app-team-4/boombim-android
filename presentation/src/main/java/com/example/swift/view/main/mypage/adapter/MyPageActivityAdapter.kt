package com.example.swift.view.main.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemMyActivityBinding
import com.boombim.android.databinding.ItemMyActivityHeaderBinding
import com.example.domain.model.MyActivityItem
import com.example.domain.model.MyActivityResponse
import com.example.swift.util.diffutil.MyActivityDiffUtil

class MyPageActivityAdapter :
    ListAdapter<MyActivityItem, RecyclerView.ViewHolder>(MyActivityDiffUtil) {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    inner class HeaderViewHolder(private val binding: ItemMyActivityHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: MyActivityItem.DateHeader) {
            binding.textDate.text = header.date
        }
    }

    inner class PlaceViewHolder(private val binding: ItemMyActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyActivityItem.ActivityItem) = with(binding) {
            val data = item.data
            textPlaceName.text = data.posName
            textTime.text = data.createdAt
            imageCongest.setImageResource(
                when (data.congestionLevel) {
                    "여유" -> R.drawable.icon_calm_small
                    "보통" -> R.drawable.icon_normal_small
                    "약간 붐빔" -> R.drawable.icon_slightly_busy_small
                    else -> R.drawable.icon_busy_small
                }
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MyActivityItem.DateHeader -> TYPE_HEADER
            is MyActivityItem.ActivityItem -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val binding = ItemMyActivityHeaderBinding.inflate(inflater, parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = ItemMyActivityBinding.inflate(inflater, parent, false)
            PlaceViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is MyActivityItem.DateHeader -> (holder as HeaderViewHolder).bind(item)
            is MyActivityItem.ActivityItem -> (holder as PlaceViewHolder).bind(item)
        }
    }
}

