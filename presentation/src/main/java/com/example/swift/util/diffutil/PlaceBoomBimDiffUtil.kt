package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.FavoriteData
import com.example.domain.model.PlaceBoomBimModel

object PlaceBoomBimDiffUtil : DiffUtil.ItemCallback<PlaceBoomBimModel>() {
    override fun areItemsTheSame(oldItem: PlaceBoomBimModel, newItem: PlaceBoomBimModel): Boolean {
        return oldItem.officialPlaceId == newItem.officialPlaceId
    }

    override fun areContentsTheSame(oldItem: PlaceBoomBimModel, newItem: PlaceBoomBimModel): Boolean {
        return oldItem == newItem
    }
}