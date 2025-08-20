package com.example.swift.view.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentChattingBinding
import com.boombim.android.databinding.FragmentSettingBinding
import com.example.swift.view.main.vote.tab.MyDiscussionTabFragment
import com.example.swift.view.main.vote.tab.VoteTabFragment
import com.google.android.material.tabs.TabLayout

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconClose.setOnClickListener {
            findNavController().navigate(
                R.id.myPageFragment, null,
                navOptions {
                    popUpTo(R.id.settingFragment) {
                        inclusive = true
                    }
                }
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}