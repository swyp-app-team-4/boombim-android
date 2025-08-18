package com.example.swift.view.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemInterestsPlaceBinding
import com.example.domain.model.InterestsPlaceModel

class InterestsPlaceAdapter (private val items: List<InterestsPlaceModel>) :
    RecyclerView.Adapter<InterestsPlaceAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemInterestsPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: InterestsPlaceModel) {
            binding.textPlaceName.text = item.placeName
            binding.textUpdate.text = item.updateContent

            val statusIconRes = when (item.status) {
                "congestion" -> R.drawable.icon_status_congestion
                "normal" -> R.drawable.icon_status_moderate
                "relaxed" -> R.drawable.icon_status_relaxation
                else -> R.drawable.icon_status_moderate
            }

            binding.iconStatus.setImageResource(statusIconRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInterestsPlaceBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}