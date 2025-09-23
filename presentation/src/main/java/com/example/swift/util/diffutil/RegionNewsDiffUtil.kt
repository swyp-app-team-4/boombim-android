package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.domain.model.RegionResponse

object RegionNewsDiffUtil : DiffUtil.ItemCallback<RegionResponse>() {
    override fun areItemsTheSame(oldItem: RegionResponse, newItem: RegionResponse): Boolean {
        return oldItem.tempId == newItem.tempId
    }

    override fun areContentsTheSame(oldItem: RegionResponse, newItem: RegionResponse): Boolean {
        return oldItem == newItem
    }
}