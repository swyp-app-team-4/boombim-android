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
import com.boombim.android.databinding.FragmentNewIssueTabBinding
import com.example.swift.view.main.notification.NotificationBaseFragment
import com.example.swift.view.main.notification.adapter.EventNotificationAdapter
import com.example.swift.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewIssueTabFragment :
    NotificationBaseFragment<FragmentNewIssueTabBinding>(FragmentNewIssueTabBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNewIssueList()
    }

    private fun initNewIssueList() = with(binding) {
        collectOnStarted {
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
