package com.example.swift.view.main.home.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemBoomBimBinding
import com.boombim.android.databinding.ItemLessBoomBimBinding
import com.bumptech.glide.Glide
import com.example.domain.model.PlaceBoomBimModel
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.swift.util.DateTimeUtils

@RequiresApi(Build.VERSION_CODES.O)
class PlaceBoomBimAdapter (private val items: List<PlaceBoomBimModel>) :
    RecyclerView.Adapter<PlaceBoomBimAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemBoomBimBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceBoomBimModel) {
            binding.textPlaceName.text = item.officialPlaceName
            binding.textTime.text = DateTimeUtils.getTimeAgo(item.observedAt)
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
                .error(R.drawable.icon_image_load_fail)
                .into(binding.imagePlace)


//            val ranking = when(item.){
//                1 -> R.drawable.icon_rank1
//                2 -> R.drawable.icon_rank2
//                3 -> R.drawable.icon_rank3
//                4 -> R.drawable.icon_rank4
//                else -> R.drawable.icon_rank5
//            }

//            binding.iconRank.setImageResource(ranking)

            binding.iconStatus.setImageResource(statusIconRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBoomBimBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}