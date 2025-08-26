package com.example.swift.view.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.domain.model.OnboardingPage

class OnboardingAdapter(
    fragmentActivity: FragmentActivity,
    private val pages: List<OnboardingPage>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        return OnBoardingFragment.newInstance(pages[position])
    }
}
