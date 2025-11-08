package com.example.swift.view.main.mypage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boombim.android.databinding.ItemMyPointHistoryBinding
import com.example.domain.model.PointHistory
import com.example.swift.util.DateTimeUtils
import com.example.swift.util.diffutil.PointHistoryDiffUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PointHistoryAdapter :
    ListAdapter<PointHistory, PointHistoryAdapter.PointHistoryViewHolder>(PointHistoryDiffUtil) {

    inner class PointHistoryViewHolder(
        val binding: ItemMyPointHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PointHistory, showDate: Boolean) = with(binding) {

            // 날짜 파싱
            val dateTime = LocalDateTime.parse(item.createdAt, DateTimeFormatter.ISO_DATE_TIME)
            val date = "${dateTime.monthValue}.${dateTime.dayOfMonth}"
            val time = "%02d:%02d".format(dateTime.hour, dateTime.minute)

            // ✅ 날짜 보여줄지 숨길지
            if (showDate) {
                textPointDate.text = date
                textPointDate.visibility = View.VISIBLE
            } else {
                textPointDate.visibility = View.INVISIBLE   // or GONE
            }

            textPointTime.text = time

            textPointDescription.text = when (item.pointCategory) {
                "CONGESTION" -> "혼잡도 알리기"
                "EVENT" -> "이벤트 응모"
                else -> item.pointCategory
            }

            // 포인트 출력
            val pointText = if (item.pointAction == "EARN") {
                "+${item.amount}"
            } else {
                "-${item.amount}"
            }
            textPoint.text = pointText

            val color = if (item.pointAction == "EARN") {
                binding.root.context.getColor(com.boombim.android.R.color.main_color)
            } else {
                binding.root.context.getColor(com.boombim.android.R.color.gray_scale_8)
            }
            textPoint.setTextColor(color)

            textCurrentPoint.text = String.format("%,d P", item.balance)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointHistoryViewHolder {
        val binding = ItemMyPointHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PointHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PointHistoryViewHolder, position: Int) {
        val item = getItem(position)

        // ✅ 날짜 표시 조건
        val showDate = if (position == 0) {
            true
        } else {
            val prev = getItem(position - 1)
            // createdAt 의 날짜(yyyy-MM-dd) 비교
            item.createdAt.substring(0, 10) != prev.createdAt.substring(0, 10)
        }

        holder.bind(item, showDate)
    }
}
