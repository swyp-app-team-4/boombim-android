package com.example.swift.view.main.setting

import android.content.Intent
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
import com.boombim.android.databinding.FragmentPolicyBinding
import com.boombim.android.databinding.FragmentSettingBinding
import com.example.swift.view.MainActivity
import com.example.swift.view.auth.SocialLoginActivity
import com.example.swift.view.dialog.LogoutDialog
import com.example.swift.view.main.vote.tab.MyDiscussionTabFragment
import com.example.swift.view.main.vote.tab.VoteTabFragment
import com.example.swift.viewmodel.SettingViewModel
import com.google.android.material.tabs.TabLayout

class SettingFragment :
    SettingBaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

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

        binding.containerAlert.setOnClickListener{
            findNavController().navigate(R.id.alertFragment)
        }

        binding.textDeleteAccount.setOnClickListener {
            findNavController().navigate(R.id.withDrawFragment)
        }

        binding.containerUse.setOnClickListener {
            val bundle = Bundle().apply {
                putString("url", "https://awesome-captain-026.notion.site/2529598992b080119479fef036d96aba?source=copy_link")
                putString("title", "이용약관")
            }

            findNavController().navigate(R.id.policyFragment, bundle)
        }

        binding.containerPrivacy.setOnClickListener {
            val bundle = Bundle().apply {
                putString("url", "https://awesome-captain-026.notion.site/2529598992b080198821d47baaf7d23f?source=copy_link")
                putString("title", "개인정보")
            }

            findNavController().navigate(R.id.policyFragment, bundle)
        }

        binding.containerServiceExplain.setOnClickListener {
            val bundle = Bundle().apply {
                putString("url", "https://awesome-captain-026.notion.site/25b9598992b08065a7ccf361e3f8ccf8?source=copy_link")
                putString("title", "이용안내")
            }

            findNavController().navigate(R.id.policyFragment, bundle)
        }

        binding.containerCustomerCenter.setOnClickListener {
            val bundle = Bundle().apply {
                putString("url", "https://awesome-captain-026.notion.site/25b9598992b0804fb058d1310b6ecdf0?source=copy_link")
                putString("title", "고객센터")
            }

            findNavController().navigate(R.id.policyFragment, bundle)
        }

        binding.containerQuestion.setOnClickListener {
            val bundle = Bundle().apply {
                putString("url", "https://naver.me/5jBB5pIh")
                putString("title", "건의사항")
            }

            findNavController().navigate(R.id.policyFragment, bundle)
        }


        binding.containerMyProfile.setOnClickListener {
            findNavController().navigate(R.id.myProfileFragment)
        }

        binding.textLogout.setOnClickListener {
            val logoutDialog = LogoutDialog(
                onConfirm = {
                    settingViewModel.logout(
                        onSuccess = {
                            val intent = Intent(requireContext(), SocialLoginActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
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
}