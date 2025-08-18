package com.example.swift.view.main.home.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.databinding.FragmentEventTabBinding
import com.boombim.android.databinding.FragmentNotificationBinding
import com.example.domain.model.NotificationModel
import com.example.swift.view.main.home.notification.adapter.EventNotificationAdapter
import com.example.swift.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventTabFragment : Fragment() {
    private var _binding: FragmentEventTabBinding? = null
    private val binding get() = _binding!!
    private val notificationViewModel: NotificationViewModel by activityViewModels()

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

        with(binding) {
            // 공지 탭
            textNotice.setOnClickListener {
                textNotice.isSelected = true
                textEvent.isSelected = false
                notificationViewModel.selectTab("ANNOUNCEMENT")
            }

            // 이벤트 탭
            textEvent.setOnClickListener {
                textNotice.isSelected = false
                textEvent.isSelected = true
                notificationViewModel.selectTab("EVENT")
            }

            // 초기 선택 상태
            textNotice.isSelected = true
            textEvent.isSelected = false
        }

    }

    private fun initEventNotification() = with(binding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                notificationViewModel.filteredNotifications.collect { list ->
                    recyclerEvent.layoutManager = LinearLayoutManager(requireContext())
                    recyclerEvent.adapter = EventNotificationAdapter(list)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}