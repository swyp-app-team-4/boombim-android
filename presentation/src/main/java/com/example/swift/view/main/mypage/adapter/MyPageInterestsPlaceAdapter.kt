package com.example.swift.view.main.mypage.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemMyPaceInterestsBinding
import com.bumptech.glide.Glide
import com.example.domain.model.FavoriteData
import com.example.swift.util.DateTimeUtils
import com.example.swift.util.diffutil.FavoriteDiffUtil

@RequiresApi(Build.VERSION_CODES.O)
class MyPageInterestsPlaceAdapter (private val items: List<FavoriteData>) :
    ListAdapter<FavoriteData, MyPageInterestsPlaceAdapter.PlaceViewHolder>(FavoriteDiffUtil) {

    inner class PlaceViewHolder(val binding: ItemMyPaceInterestsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteData) {
            binding.textPlaceName.text = item.name
            binding.textUpdate.text = DateTimeUtils.getTimeAgo(item.observedAt.toString())

            val statusIconRes = when (item.congestionLevelName) {
                "붐빔" -> R.drawable.icon_busy_small
                "약간 붐빔" -> R.drawable.icon_slightly_busy_small
                "여유" -> R.drawable.icon_calm_small
                else -> R.drawable.icon_normal_small
            }

            Glide.with(binding.imagePlace.context)
                .load(item.imageUrl)
                .centerCrop()
                .error(R.drawable.image_load_fail_large)
                .into(binding.imagePlace)

            binding.iconStatus.setImageResource(statusIconRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyPaceInterestsBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}