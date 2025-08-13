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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                notificationViewModel.notificationList.collect{ notificationList ->
                    val adapter = EventNotificationAdapter(notificationList.filter { it.alarmType == "이벤트" })
                    recyclerEvent.layoutManager = LinearLayoutManager(requireContext())
                    recyclerEvent.adapter = adapter
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}