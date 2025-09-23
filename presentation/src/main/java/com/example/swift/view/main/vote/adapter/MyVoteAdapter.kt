package com.example.swift.view.main.vote.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemMyVoteBinding
import com.bumptech.glide.Glide
import com.example.domain.model.MyVoteItem
import com.example.swift.util.DateTimeUtils
import com.example.swift.util.diffutil.MyVoteDiffUtil

class MyVoteAdapter (
    private val items: List<MyVoteItem>,
    private val onBtnClick: (MyVoteItem) -> Unit
    ) :
    ListAdapter<MyVoteItem,MyVoteAdapter.PlaceViewHolder>(MyVoteDiffUtil) {

    inner class PlaceViewHolder(val binding: ItemMyVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(item: MyVoteItem) {

            binding.btnEndVote.setOnClickListener {
                onBtnClick(item)
            }

            val imageViews = listOf(binding.userImg1, binding.userImg2, binding.userImg3)

            item.profile.take(3).forEachIndexed { index, url ->
                imageViews[index].visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(url)
                    .placeholder(R.drawable.icon_gray_circle)
                    .circleCrop()
                    .into(imageViews[index])
            }

            binding.textPeopleInterests.text = item.voteDuplicationCnt.toString()
            binding.textTitle.text = item.posName
            binding.textTime.text = DateTimeUtils.getTimeAgo(item.createdAt)
            binding.textCalmCount.text =item.relaxedCnt.toString()
            binding.textNormalCount.text =item.commonly.toString()
            binding.textSlightlyBusyCount.text =item.slightlyBusyCnt.toString()
            binding.textBusyCount.text =item.crowedCnt.toString()
            binding.textTotalCount.text = (item.relaxedCnt + item.commonly + item.slightlyBusyCnt + item.crowedCnt).toString()

            binding.progressCalm.progress = item.relaxedCnt
            binding.progressNormal.progress = item.commonly
            binding.progressSlightlyBusy.progress = item.slightlyBusyCnt
            binding.progressBusy.progress = item.crowedCnt

            if (item.voteStatus == "END") {
                binding.btnEndVote.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.gray_scale_4) // 회색으로
                )
                binding.btnEndVote.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.gray_scale_7) // 흰색으로
                )
                binding.btnEndVote.setBackgroundResource(R.drawable.bg_rounded_gray3)
            }

            binding.btnEndVote.isEnabled = item.voteStatus != "END"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyVoteBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}