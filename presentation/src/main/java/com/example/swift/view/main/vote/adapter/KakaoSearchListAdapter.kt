package com.example.swift.view.main.vote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.databinding.ItemKakaoSearchBinding
import com.example.domain.model.PlaceDocumentDto

class KakaoSearchListAdapter (
    private val items: List<PlaceDocumentDto>,
    private val onItemClick: (PlaceDocumentDto) -> Unit
) :
    RecyclerView.Adapter<KakaoSearchListAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemKakaoSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceDocumentDto) {
            binding.textPlaceName.text = item.place_name

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemKakaoSearchBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}