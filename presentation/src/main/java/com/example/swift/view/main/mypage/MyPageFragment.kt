package com.example.swift.view.main.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMyPageBinding
import com.bumptech.glide.Glide
import com.example.swift.view.main.home.notification.tab.EventTabFragment
import com.example.swift.view.main.home.notification.tab.NewIssueTabFragment
import com.example.swift.view.main.mypage.tab.MyPageInterestsTabFragment
import com.example.swift.view.main.mypage.tab.MyPageMyVoteTabFragment
import com.example.swift.view.main.mypage.tab.MyPageVoteTabFragment
import com.example.swift.viewmodel.MyPageViewModel
import com.example.swift.viewmodel.VoteViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayout()

        observeProfile()

        binding.iconSetting.setOnClickListener {
            findNavController().navigate(R.id.settingFragment)
        }

        binding.iconProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }

    }

    private fun observeProfile() {
        lifecycleScope.launch {
            myPageViewModel.profile.collect { profile ->
                binding.textNickName.text = profile.name
                binding.textVoteCount.text = "투표 | ${profile.voteCnt}"
                binding.textMyVoteCount.text = "질문 | ${profile.questionCnt}"

                Glide.with(requireContext())
                    .load(profile.profile) // JSON에서 내려온 이미지 URL
                    .placeholder(R.drawable.icon_edit_profile) // 로딩 중 보여줄 기본 이미지
                    .error(R.drawable.icon_edit_profile) // 실패 시 보여줄 기본 이미지
                    .into(binding.iconProfile)
            }
        }
    }

    private fun initTabLayout(){
        childFragmentManager.beginTransaction()
            .replace(R.id.flame, MyPageInterestsTabFragment())
            .commit()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val fragment = when (tab?.position) {
                    0 -> MyPageInterestsTabFragment()
                    1 -> MyPageVoteTabFragment()
                    2 -> MyPageMyVoteTabFragment()
                    else -> MyPageInterestsTabFragment()
                }

                childFragmentManager.beginTransaction()
                    .replace(R.id.flame, fragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}