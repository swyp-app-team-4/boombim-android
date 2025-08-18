package com.example.swift.view.main.discussion.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.databinding.ItemKakaoSearchBinding
import com.boombim.android.databinding.ItemMyVoteBinding
import com.example.domain.model.MyVoteItem
import com.example.domain.model.PlaceDocumentDto
import com.example.swift.util.DateTimeUtils

class MyVoteAdapter (private val items: List<MyVoteItem>) :
    RecyclerView.Adapter<MyVoteAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemMyVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(item: MyVoteItem) {
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

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyVoteBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}