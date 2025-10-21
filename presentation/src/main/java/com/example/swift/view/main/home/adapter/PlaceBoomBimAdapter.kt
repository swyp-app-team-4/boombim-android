package com.example.swift.view.main.home.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemBoomBimBinding
import com.boombim.android.databinding.ItemLessBoomBimBinding
import com.bumptech.glide.Glide
import com.example.domain.model.FavoriteData
import com.example.domain.model.PlaceBoomBimModel
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.swift.util.DateTimeUtils
import com.example.swift.util.diffutil.FavoriteDiffUtil
import com.example.swift.util.diffutil.PlaceBoomBimDiffUtil

@RequiresApi(Build.VERSION_CODES.O)
class PlaceBoomBimAdapter (
    private val items: List<PlaceBoomBimModel>
) : ListAdapter<PlaceBoomBimModel,PlaceBoomBimAdapter.PlaceViewHolder>(PlaceBoomBimDiffUtil) {

    inner class PlaceViewHolder(val binding: ItemBoomBimBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceBoomBimModel) {
            binding.textPlaceName.text = item.officialPlaceName
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

        val rankingRes = when (position) {
            0 -> R.drawable.icon_rank1
            1 -> R.drawable.icon_rank2
            2 -> R.drawable.icon_rank3
            3 -> R.drawable.icon_rank4
            else -> R.drawable.icon_rank5
        }

        holder.binding.iconRank.setImageResource(rankingRes)
    }


    override fun getItemCount() = items.size
}