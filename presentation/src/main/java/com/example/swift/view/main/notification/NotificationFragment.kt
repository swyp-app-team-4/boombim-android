package com.example.swift.view.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentEventTabBinding
import com.boombim.android.databinding.FragmentNotificationBinding
import com.example.swift.view.main.notification.tab.EventTabFragment
import com.example.swift.view.main.notification.tab.NewIssueTabFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    NotificationBaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayout()

        binding.iconClose.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment, null,
                navOptions {
                    popUpTo(R.id.notificationFragment) {
                        inclusive = true
                    }
                }
            )
        }

    }

    private fun initTabLayout(){
        childFragmentManager.beginTransaction()
            .replace(R.id.flame, NewIssueTabFragment())
            .commit()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val fragment = when (tab?.position) {
                    0 -> NewIssueTabFragment()
                    1 -> EventTabFragment()
                    else -> NewIssueTabFragment()
                }

                childFragmentManager.beginTransaction()
                    .replace(R.id.flame, fragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}