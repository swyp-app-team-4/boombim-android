package com.example.swift.view.main.notification.tab

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
import com.example.swift.view.main.notification.NotificationBaseFragment
import com.example.swift.view.main.notification.adapter.EventNotificationAdapter
import com.example.swift.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventTabFragment :
    NotificationBaseFragment<FragmentEventTabBinding>(FragmentEventTabBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEventNotification()

        with(binding) {
            textNotice.setOnClickListener {
                textNotice.isSelected = true
                textEvent.isSelected = false
                notificationViewModel.selectTab("ANNOUNCEMENT")
            }
            textEvent.setOnClickListener {
                textNotice.isSelected = false
                textEvent.isSelected = true
                notificationViewModel.selectTab("EVENT")
            }
            textNotice.isSelected = true
            textEvent.isSelected = false
        }
    }

    private fun initEventNotification() = with(binding) {
        collectOnStarted {
            notificationViewModel.filteredNotifications.collect { list ->
                recyclerEvent.layoutManager = LinearLayoutManager(requireContext())
                recyclerEvent.adapter = EventNotificationAdapter(list)
            }
        }
    }
}
