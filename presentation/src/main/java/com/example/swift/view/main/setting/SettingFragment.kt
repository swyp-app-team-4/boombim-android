package com.example.swift.view.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentChattingBinding
import com.boombim.android.databinding.FragmentSettingBinding
import com.example.swift.view.dialog.LogoutDialog
import com.example.swift.view.main.vote.tab.MyDiscussionTabFragment
import com.example.swift.view.main.vote.tab.VoteTabFragment
import com.example.swift.viewmodel.SettingViewModel
import com.google.android.material.tabs.TabLayout

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val settingViewModel: SettingViewModel by activityViewModels()

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

        binding.textLogout.setOnClickListener {
            val logoutDialog = LogoutDialog(
                onConfirm = {
                    settingViewModel.logout(
                        onSuccess = {
                            findNavController().navigate(
                                R.id.socialLoginFragment,
                                null,
                                navOptions {
                                    popUpTo(R.id.socialLoginFragment) { // 네비게이션 그래프의 시작 지점 ID
                                        inclusive = true
                                    }
                                }
                            )


                        },
                        onFail = { msg ->
                           Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
            logoutDialog.show(parentFragmentManager, "LogoutDialog")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}