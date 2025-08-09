package com.example.swift.view.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.databinding.FragmentEventTabBinding
import com.boombim.android.databinding.FragmentNotificationBinding
import com.example.domain.model.NotificationModel
import com.example.swift.view.main.notification.adapter.EventNotificationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventTabFragment : Fragment() {
    private var _binding: FragmentEventTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEventTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEventNotification()

        with(binding){
            textNotice.setOnClickListener {
                binding.textNotice.isSelected = true
                binding.textEvent.isSelected = false
            }

            textEvent.setOnClickListener {
                binding.textNotice.isSelected = false
                binding.textEvent.isSelected = true
            }
        }

    }

    private fun initEventNotification() = with(binding){
        val dummyList = listOf(
            NotificationModel("새로운 이벤트가 도착했어요!", "5분 전", false),
            NotificationModel("리워드가 적립되었습니다.", "10분 전", true),
            NotificationModel("친구가 메시지를 보냈어요.", "1시간 전", false)
        )

        val adapter = EventNotificationAdapter(dummyList)
        recyclerEvent.layoutManager = LinearLayoutManager(requireContext())
        recyclerEvent.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}