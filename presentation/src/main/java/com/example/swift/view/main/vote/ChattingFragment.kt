package com.example.swift.view.main.vote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.boombim.android.R
import com.boombim.android.databinding.FragmentChattingBinding
import com.example.swift.view.main.vote.tab.MyDiscussionTabFragment
import com.example.swift.view.main.vote.tab.VoteTabFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChattingFragment : BaseViewBindingFragment<FragmentChattingBinding>(
    FragmentChattingBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayout()

        binding.btnMakeVote.setOnClickListener {
            findNavController().navigate(R.id.makeVoteFragment)
        }
    }

    private fun initTabLayout() {
        // 초기 Fragment 설정
        childFragmentManager.beginTransaction()
            .replace(R.id.flame, VoteTabFragment())
            .commit()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val fragment = when (tab?.position) {
                    0 -> VoteTabFragment()
                    1 -> MyDiscussionTabFragment()
                    else -> VoteTabFragment()
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
