package com.example.swift.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.boombim.android.databinding.FragmentOnBoardingBinding
import com.example.domain.model.OnboardingPage

class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    val binding: FragmentOnBoardingBinding
        get() = _binding!!

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_DESC = "desc"
        private const val ARG_IMAGE = "image"

        fun newInstance(page: OnboardingPage) = OnBoardingFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, page.title)
                putString(ARG_DESC, page.description)
                putInt(ARG_IMAGE, page.imageRes)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val title = it.getString(ARG_TITLE)
            val description = it.getString(ARG_DESC)
            val imageRes = it.getInt(ARG_IMAGE)

            binding.textTitle.text = title
            binding.textSub.text = description
            binding.imageOnboarding.setImageResource(imageRes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}