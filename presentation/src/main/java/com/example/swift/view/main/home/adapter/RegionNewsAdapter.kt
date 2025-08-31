package com.example.swift.view.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.databinding.ItemRegionNewsBinding
import com.example.domain.model.RegionNewsModel
import com.example.domain.model.RegionResponse

class RegionNewsAdapter(private val items: List<RegionResponse>) : RecyclerView.Adapter<RegionNewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: ItemRegionNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RegionResponse) {
            binding.textTitle.text = "${item.area} 집회예정"
            binding.textContent.text = "오늘 ${item.startTime}, ${item.posName}에서 약 ${item.peopleCnt}명 규모 집회 예정되어 있습니다.  해당 시간대 혼잡이 예상되니 이동 시 유의하세요."
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRegionNewsBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
