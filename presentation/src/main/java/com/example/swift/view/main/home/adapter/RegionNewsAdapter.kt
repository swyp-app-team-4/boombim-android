package com.example.swift.view.main.home.adapter

import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.font.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.R
import com.boombim.android.databinding.ItemRegionNewsBinding
import com.example.domain.model.RegionResponse
import com.example.swift.util.CustomTypefaceSpan

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class RegionNewsAdapter(private val items: List<RegionResponse>) :
    RecyclerView.Adapter<RegionNewsAdapter.NewsViewHolder>() {

    private val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    private val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")

    inner class NewsViewHolder(val binding: ItemRegionNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RegionResponse?) {
            if (item == null) {
                binding.imageKoreaMark.visibility = android.view.View.GONE
                binding.textKoreaMinistry.visibility = android.view.View.GONE
                binding.textNoNews.visibility = android.view.View.VISIBLE
            } else {
                binding.textNoNews.visibility = android.view.View.GONE

                val start = LocalDateTime.parse(item.startTime, inputFormatter)
                val end = LocalDateTime.parse(item.endTime, inputFormatter)
                val timeRange = "${start.format(outputFormatter)} ~ ${end.format(outputFormatter)}"

                binding.textTitle.text = "${item.area} 집회예정"

                val content = "오늘 $timeRange, ${item.posName}에서 약 ${item.peopleCnt}명 규모 집회 예정되어 있습니다. 해당 시간대 혼잡이 예상되니 이동 시 유의하세요."
                val spannable = SpannableString(content)


                val customTypeface = ResourcesCompat.getFont(binding.root.context, R.font.pretend_bold)!!

                val timeStart = content.indexOf(timeRange)
                spannable.setSpan(
                    CustomTypefaceSpan(customTypeface),
                    timeStart,
                    timeStart + timeRange.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                val posStart = content.indexOf(item.posName)
                spannable.setSpan(
                    CustomTypefaceSpan(customTypeface),
                    posStart,
                    posStart + item.posName.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                val peopleStr = "${item.peopleCnt}명"
                val peopleStart = content.indexOf(peopleStr)
                spannable.setSpan(
                    CustomTypefaceSpan(customTypeface),
                    peopleStart,
                    peopleStart + peopleStr.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.textContent.text = spannable

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRegionNewsBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        if (items.isEmpty()) {
            holder.bind(null)
        } else {
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int = if (items.isEmpty()) 1 else items.size
}
