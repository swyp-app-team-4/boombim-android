package com.example.swift.view.main.map.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemMemberPlaceDetailBinding
import com.boombim.android.databinding.ItemNearByBinding
import com.bumptech.glide.Glide
import com.example.domain.model.CongestionData
import com.example.domain.model.MemberCongestionItem
import com.example.swift.util.DateTimeUtils
import com.example.swift.util.diffutil.NearByDiffUtil

class NearByAdapter  (private var items: List<CongestionData>) :
    ListAdapter<CongestionData, NearByAdapter.PlaceViewHolder>(NearByDiffUtil) {

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<CongestionData>) {
        this.items = newItems
    }

    inner class PlaceViewHolder(val binding: ItemNearByBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CongestionData) {
            binding.textPlaceName.text = item.officialPlaceName
            binding.textUpdateTime.text = "방금전"
            binding.textPlaceAddress.text = item.legalDong

            val statusIconRes = when (item.congestionLevelName) {
                "붐빔" -> R.drawable.icon_busy_small
                "약간 붐빔" -> R.drawable.icon_slightly_busy_small
                "여유" -> R.drawable.icon_calm_small
                else -> R.drawable.icon_normal_small
            }

            Glide.with(binding.imagePlace.context)
                .load(item.imageUrl)
                .centerCrop()
                .error(R.drawable.icon_gray_circle)
                .into(binding.imagePlace)

            binding.iconBoombim.setImageResource(statusIconRes)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNearByBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}