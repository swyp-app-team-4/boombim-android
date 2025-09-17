package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.PlaceBoomBimModel
import com.example.domain.model.PlaceLessBoomBimModel

object PlaceLessBoomBimDiffUtil : DiffUtil.ItemCallback<PlaceLessBoomBimModel>() {
    override fun areItemsTheSame(oldItem: PlaceLessBoomBimModel, newItem: PlaceLessBoomBimModel): Boolean {
        return oldItem.officialPlaceId == newItem.officialPlaceId
    }

    override fun areContentsTheSame(oldItem: PlaceLessBoomBimModel, newItem: PlaceLessBoomBimModel): Boolean {
        return oldItem == newItem
    }
}