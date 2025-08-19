package com.example.swift.view.main.vote.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemVoteBinding
import com.example.domain.model.VoteItem
import com.example.swift.util.DateTimeUtils

class VoteAdapter(
    private val onVoteClick: (VoteItem) -> Unit
) : ListAdapter<VoteItem, VoteAdapter.PlaceViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<VoteItem>() {
        override fun areItemsTheSame(oldItem: VoteItem, newItem: VoteItem) =
            oldItem.voteId == newItem.voteId

        override fun areContentsTheSame(oldItem: VoteItem, newItem: VoteItem): Boolean {
            return oldItem.voteId == newItem.voteId &&
                    oldItem.voteFlag == newItem.voteFlag &&
                    oldItem.selectedIcon == newItem.selectedIcon
        }
    }


    inner class PlaceViewHolder(val binding: ItemVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(item: VoteItem) {
            binding.textTitle.text = item.posName
            binding.textPeopleInterests.text = item.voteDuplicationCnt.toString()
            binding.textTime.text = DateTimeUtils.getTimeAgo(item.createdAt)

            updateIcons(item)

            if (item.voteFlag) {
                binding.btnVote.setBackgroundResource(R.drawable.icon_completed_vote)
                binding.btnVote.isEnabled = false

                binding.imgCalm.setOnClickListener(null)
                binding.imgNormal.setOnClickListener(null)
                binding.iconSlightlyBusy.setOnClickListener(null)
                binding.iconBusy.setOnClickListener(null)
            } else{
                binding.imgCalm.setOnClickListener { selectIcon(item, 0) }
                binding.imgNormal.setOnClickListener { selectIcon(item, 1) }
                binding.iconSlightlyBusy.setOnClickListener { selectIcon(item, 2) }
                binding.iconBusy.setOnClickListener { selectIcon(item, 3) }

                binding.btnVote.setOnClickListener {
                    onVoteClick(item)
                }
            }

        }

        private fun selectIcon(item: VoteItem, iconIndex: Int) {
            item.selectedIcon = iconIndex
            updateIcons(item)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVoteBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
