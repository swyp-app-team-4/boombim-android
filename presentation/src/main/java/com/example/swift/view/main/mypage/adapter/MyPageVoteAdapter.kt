package com.example.swift.view.main.mypage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemEventNotificationBinding
import com.boombim.android.databinding.ItemMyPageVoteBinding
import com.bumptech.glide.Glide
import com.example.domain.model.NotificationModel
import com.example.domain.model.PopularityDetail
import com.example.swift.util.DateTimeUtils
import com.example.swift.view.main.notification.adapter.EventNotificationAdapter

class MyPageVoteAdapter (private val items: List<PopularityDetail>) :
    RecyclerView.Adapter<MyPageVoteAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private val binding: ItemMyPageVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isExpanded = false

        @SuppressLint("SetTextI18n")
        fun bind(item: PopularityDetail) {

            binding.textPeopleInterests.text = item.voteDuplicationCnt.toString()
            binding.textTitle.text = item.posName
            binding.textCalmCount.text =item.relaxedCnt.toString()
            binding.textNormalCount.text =item.commonly.toString()
            binding.textSlightlyBusyCount.text =item.slightlyBusyCnt.toString()
            binding.textBusyCount.text =item.crowedCnt.toString()

            binding.progressCalm.progress = item.relaxedCnt
            binding.progressNormal.progress = item.commonly
            binding.progressSlightlyBusy.progress = item.slightlyBusyCnt
            binding.progressBusy.progress = item.crowedCnt

            if (item.popularRes.isNotEmpty()) {
                val icon1 = getBoombimIcon(item.popularRes[0])
                if (icon1 != null) {
                    binding.imageBoombim1.setImageResource(icon1)
                    binding.imageBoombim1.visibility = View.VISIBLE
                } else {
                    binding.imageBoombim1.visibility = View.GONE
                }
            } else {
                binding.imageBoombim1.visibility = View.GONE
            }

            if (item.popularRes.size >= 2) {
                val icon2 = getBoombimIcon(item.popularRes[1])
                if (icon2 != null) {
                    binding.imageBoombim2.setImageResource(icon2)
                    binding.imageBoombim2.visibility = View.VISIBLE
                } else {
                    binding.imageBoombim2.visibility = View.GONE
                }
            } else {
                binding.imageBoombim2.visibility = View.GONE
            }



            val imageViews = listOf(binding.userImg1, binding.userImg2, binding.userImg3)

            imageViews.forEach { imageView ->
                imageView.setImageResource(R.drawable.icon_gray_circle)
            }

            item.profile.take(3).forEachIndexed { index, url ->
                imageViews[index].visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(url)
                    .placeholder(R.drawable.icon_gray_circle)
                    .error(R.drawable.icon_gray_circle)
                    .circleCrop()
                    .into(imageViews[index])
            }


            binding.iconArrow.setOnClickListener {
                isExpanded = !isExpanded
                binding.layoutGraphs.visibility = if (isExpanded) {
                    binding.iconArrow.setImageResource(R.drawable.icon_bottom_arrow)
                    View.VISIBLE
                } else {
                    binding.iconArrow.setImageResource(R.drawable.icon_top_arrow)
                    View.GONE
                }
            }
        }
    }

    private fun getBoombimIcon(res: String): Int? {
        return when (res) {
            "RELAXED" -> R.drawable.icon_calm_small
            "COMMONLY" -> R.drawable.icon_normal_small
            "BUSY" -> R.drawable.icon_slightly_busy_small
            "CROWDED" -> R.drawable.icon_busy_small
            else -> null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyPageVoteBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

}