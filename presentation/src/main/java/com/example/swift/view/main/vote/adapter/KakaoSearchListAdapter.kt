package com.example.swift.view.main.vote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.databinding.ItemKakaoSearchBinding
import com.example.domain.model.PlaceDocumentDto
import com.example.swift.util.diffutil.KakaoListDiffUtil

class KakaoSearchListAdapter(
    private val onItemClick: (PlaceDocumentDto) -> Unit
) : PagingDataAdapter<PlaceDocumentDto, KakaoSearchListAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(
        private val binding: ItemKakaoSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlaceDocumentDto) {
            binding.textPlaceName.text = item.place_name

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKakaoSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    companion object {

        val DIFF = object : DiffUtil.ItemCallback<PlaceDocumentDto>() {

            override fun areItemsTheSame(
                oldItem: PlaceDocumentDto,
                newItem: PlaceDocumentDto
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PlaceDocumentDto,
                newItem: PlaceDocumentDto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}