package com.example.swift.view.main.map.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemMemberPlaceDetailBinding
import com.boombim.android.databinding.ItemMyPaceInterestsBinding
import com.bumptech.glide.Glide
import com.example.domain.model.FavoriteData
import com.example.domain.model.MemberCongestionItem
import com.example.swift.util.DateTimeUtils
import com.example.swift.view.main.mypage.adapter.MyPageInterestsPlaceAdapter

@RequiresApi(Build.VERSION_CODES.O)
class MemberPlaceDetailAdapter (private var items: List<MemberCongestionItem>) :
    RecyclerView.Adapter<MemberPlaceDetailAdapter.PlaceViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<MemberCongestionItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    inner class PlaceViewHolder(val binding: ItemMemberPlaceDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MemberCongestionItem) {
            binding.textName.text = item.memberName
            binding.textTime.text = DateTimeUtils.getTimeAgo(item.createdAt)
            binding.textContent.text = item.congestionLevelMessage

            val statusIconRes = when (item.congestionLevelName) {
                "붐빔" -> R.drawable.icon_busy_small
                "약간 붐빔" -> R.drawable.icon_slightly_busy_small
                "여유" -> R.drawable.icon_calm_small
                else -> R.drawable.icon_normal_small
            }

            Glide.with(binding.imageProfile.context)
                .load(item.memberProfile)
                .centerCrop()
                .error(R.drawable.icon_gray_circle)
                .into(binding.imageProfile)

            binding.iconStatus.setImageResource(statusIconRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMemberPlaceDetailBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}