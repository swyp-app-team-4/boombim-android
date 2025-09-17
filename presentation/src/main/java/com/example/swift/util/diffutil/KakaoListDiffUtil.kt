package com.example.swift.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.MyVoteItem
import com.example.domain.model.PlaceDocumentDto

object KakaoListDiffUtil : DiffUtil.ItemCallback<PlaceDocumentDto>() {
    override fun areItemsTheSame(oldItem: PlaceDocumentDto, newItem: PlaceDocumentDto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlaceDocumentDto, newItem: PlaceDocumentDto): Boolean {
        return oldItem == newItem
    }
}