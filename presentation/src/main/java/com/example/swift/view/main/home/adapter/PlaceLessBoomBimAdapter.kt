package com.example.swift.view.main.home.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemLessBoomBimBinding
import com.bumptech.glide.Glide
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.swift.util.DateTimeUtils
import com.example.swift.util.diffutil.PlaceLessBoomBimDiffUtil

@RequiresApi(Build.VERSION_CODES.O)
class PlaceLessBoomBimAdapter(
    private val items: List<PlaceLessBoomBimModel>,
    private val onItemClick: (PlaceLessBoomBimModel) -> Unit
) :
    ListAdapter<PlaceLessBoomBimModel,PlaceLessBoomBimAdapter.PlaceViewHolder>(PlaceLessBoomBimDiffUtil) {

    inner class PlaceViewHolder(val binding: ItemLessBoomBimBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceLessBoomBimModel) {
            binding.textPlaceName.text = item.officialPlaceName
            binding.textTime.text = DateTimeUtils.getTimeAgo(item.observedAt)

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

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLessBoomBimBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}