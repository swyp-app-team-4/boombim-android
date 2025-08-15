package com.example.swift.view.main.discussion.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemDiscussionVoteBinding
import com.example.domain.model.VoteItem
import com.example.domain.model.VoteModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class VoteAdapter(
    private val items: List<VoteItem>,
    private val onVoteClick: (VoteItem) -> Unit
) : RecyclerView.Adapter<VoteAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemDiscussionVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(item: VoteItem) {
            binding.textTitle.text = item.posName
            binding.textPeopleInterests.text = item.voteDuplicationCnt.toString()
            binding.textTime.text = getTimeAgo(item.createdAt)

            updateIcons(item)

            // 각 이미지 클릭 리스너
            binding.imgCalm.setOnClickListener { selectIcon(item, 0) }
            binding.imgNormal.setOnClickListener { selectIcon(item, 1) }
            binding.iconSlightlyBusy.setOnClickListener { selectIcon(item, 2) }
            binding.iconBusy.setOnClickListener { selectIcon(item, 3) }

            binding.btnVote.setOnClickListener {
                onVoteClick(item)
            }
        }

        private fun selectIcon(item: VoteItem, iconIndex: Int) {
            item.selectedIcon = iconIndex
            updateIcons(item) // 아이콘만 갱신
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun updateIcons(item: VoteItem) {
            binding.imgCalm.setImageResource(
                when (item.selectedIcon) {
                    0 -> R.drawable.icon_calm_v2
                    -1 -> R.drawable.icon_calm_v1
                    else -> R.drawable.icon_calm_v3
                }
            )
            binding.imgNormal.setImageResource(
                when (item.selectedIcon) {
                    1 -> R.drawable.icon_normal_v2
                    -1 -> R.drawable.icon_normal_v1
                    else -> R.drawable.icon_normal_v3
                }
            )
            binding.iconSlightlyBusy.setImageResource(
                when (item.selectedIcon) {
                    2 -> R.drawable.icon_slightly_busy_v2
                    -1 -> R.drawable.icon_slightly_busy_v1
                    else -> R.drawable.icon_slightly_busy_v3
                }
            )
            binding.iconBusy.setImageResource(
                when (item.selectedIcon) {
                    3 -> R.drawable.icon_busy_v2
                    -1 -> R.drawable.icon_busy_v1
                    else -> R.drawable.icon_busy_v3
                }
            )
            binding.btnVote.background = when (item.selectedIcon) {
                -1 -> binding.root.context.getDrawable(R.drawable.bg_btn_vote_gray)
                else -> binding.root.context.getDrawable(R.drawable.bg_btn_vote_main_color)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeAgo(createdAt: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val createdTime = LocalDateTime.parse(createdAt, formatter)
        val now = LocalDateTime.now(ZoneId.systemDefault())

        val duration = Duration.between(createdTime, now)
        val minutes = duration.toMinutes()
        val hours = duration.toHours()
        val days = duration.toDays()

        return when {
            minutes < 1 -> "방금전"
            minutes < 60 -> "${minutes}분 전"
            hours < 24 -> "${hours}시간전"
            days < 30 -> "${days}일전"
            else -> createdAt.substring(0, 10)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDiscussionVoteBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
