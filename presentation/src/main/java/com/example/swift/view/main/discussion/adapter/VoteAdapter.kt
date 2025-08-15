package com.example.swift.view.main.discussion.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemDiscussionVoteBinding
import com.example.domain.model.VoteModel

class VoteAdapter(
    private val items: List<VoteModel>,
    private val onVoteClick: (VoteModel) -> Unit
) : RecyclerView.Adapter<VoteAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemDiscussionVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VoteModel) {
            binding.textTitle.text = item.title
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

        private fun selectIcon(item: VoteModel, iconIndex: Int) {
            item.selectedIcon = iconIndex
            updateIcons(item) // 아이콘만 갱신
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun updateIcons(item: VoteModel) {
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
        val binding = ItemDiscussionVoteBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
