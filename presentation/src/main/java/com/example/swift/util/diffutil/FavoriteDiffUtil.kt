package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.FavoriteData

object FavoriteDiffUtil: DiffUtil.ItemCallback<FavoriteData>() {
    override fun areItemsTheSame(oldItem: FavoriteData, newItem: FavoriteData): Boolean {
        return oldItem.favoriteId == newItem.favoriteId
    }

    override fun areContentsTheSame(oldItem: FavoriteData, newItem: FavoriteData): Boolean {
        return oldItem == newItem
    }
}