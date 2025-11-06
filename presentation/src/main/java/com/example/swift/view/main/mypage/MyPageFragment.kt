package com.example.swift.view.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMyPageBinding
import com.bumptech.glide.Glide
import com.example.domain.model.ProfileModel
import com.example.swift.view.main.mypage.tab.MyPageActivityTabFragment
import com.example.swift.view.main.mypage.tab.MyPageInterestsTabFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : MyPageBaseFragment<FragmentMyPageBinding>(
    FragmentMyPageBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        observeProfile()
    }

    override fun onResume() {
        super.onResume()
        myPageViewModel.refreshProfile()
    }

    private fun initUI() {
        initTabLayout()
        binding.iconSetting.setOnClickListener {
            navigateTo(R.id.settingFragment)
        }
        binding.iconProfile.setOnClickListener {
            navigateTo(R.id.editProfileFragment)
        }
        binding.containerPoint.setOnClickListener {
            navigateTo(R.id.myPointDetailFragment)
        }
    }

    private fun observeProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myPageViewModel.profile.collect { profile ->
                    updateProfileUI(profile)
                }
            }
        }
    }


    private fun updateProfileUI(profile: ProfileModel) {
        binding.textNickName.text = profile.name

        lifecycleScope.launch {
            myPageViewModel.myPoint.collect { point ->
                binding.textPoint.text = "${point}"
            }
        }

        Glide.with(this)
            .load(profile.profile)
            .placeholder(R.drawable.icon_gray_circle)
            .error(R.drawable.icon_gray_circle)
            .into(binding.iconProfile)
    }

    private fun initTabLayout() {
        setFragment(MyPageInterestsTabFragment())

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val fragment = when (tab?.position) {
                    0 -> MyPageInterestsTabFragment()
                    1 -> MyPageActivityTabFragment()
                    else -> MyPageInterestsTabFragment()
                }
                setFragment(fragment)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.flame, fragment)
            .commit()
    }

    private fun navigateTo(destinationId: Int) {
        findNavController().navigate(destinationId)
    }
}
