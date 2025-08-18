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
import com.boombim.android.databinding.FragmentNewIssueTabBinding
import com.boombim.android.databinding.FragmentNotificationBinding
import com.example.swift.view.main.home.notification.adapter.EventNotificationAdapter
import com.example.swift.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewIssueTabFragment : Fragment() {
    private var _binding: FragmentNewIssueTabBinding? = null
    private val binding get() = _binding!!
    private val notificationViewModel: NotificationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewIssueTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNewIssueList()
    }

    private fun initNewIssueList() = with(binding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                notificationViewModel.voteAndCommunicationNotifications.collect { list ->

                    if (list.isEmpty()) {
                        recyclerNewIssue.visibility = View.GONE
                        addPlaceLayout.visibility = View.VISIBLE
                    } else {
                        recyclerNewIssue.visibility = View.VISIBLE
                        addPlaceLayout.visibility = View.GONE

                        recyclerNewIssue.layoutManager = LinearLayoutManager(requireContext())
                        recyclerNewIssue.adapter = EventNotificationAdapter(list)
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}