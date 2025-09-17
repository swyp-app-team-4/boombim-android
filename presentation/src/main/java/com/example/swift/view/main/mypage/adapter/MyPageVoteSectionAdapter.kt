package com.example.swift.view.main.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.databinding.ItemVoteSectionBinding
import com.example.domain.model.MyPageVoteResponse
import com.example.swift.util.diffutil.MyPageVoteSectionDiffUtil
import java.text.SimpleDateFormat
import java.util.Locale

class MyPageVoteSectionAdapter(private val items: List<MyPageVoteResponse>) :
    ListAdapter<MyPageVoteResponse, MyPageVoteSectionAdapter.SectionViewHolder>(MyPageVoteSectionDiffUtil) {

    inner class SectionViewHolder(private val binding: ItemVoteSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyPageVoteResponse) {
            binding.textDateSection.text = formatDate(item.day)

            val childAdapter = MyPageVoteAdapter(item.res)
            binding.recyclerVotes.apply {
                adapter = childAdapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
        }

        private fun formatDate(dateStr: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val outputFormat = SimpleDateFormat("yyyy. MM. dd (E)", Locale.KOREAN)
            val date = inputFormat.parse(dateStr)
            return outputFormat.format(date!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVoteSectionBinding.inflate(inflater, parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
