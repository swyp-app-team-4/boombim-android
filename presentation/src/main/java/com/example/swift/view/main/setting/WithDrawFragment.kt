package com.example.swift.view.main.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentSettingBinding
import com.boombim.android.databinding.FragmentWithdrawBinding
import com.example.swift.view.MainActivity
import com.example.swift.view.auth.SocialLoginActivity
import com.example.swift.viewmodel.SettingViewModel

class WithDrawFragment :
    SettingBaseFragment<FragmentWithdrawBinding>(FragmentWithdrawBinding::inflate) {

    private lateinit var checkBoxes: List<CheckBox>
    private lateinit var cbEtc: CheckBox

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkBoxes = listOf(
            binding.cbReason1,
            binding.cbReason2,
            binding.cbReason3,
            binding.cbReason4,
            binding.cbReason5,
            binding.cbReason6
        )
        cbEtc = binding.cbReason6 // "기타"

        // ✅ 체크박스를 라디오버튼처럼 하나만 선택되도록 처리
        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { button, isChecked ->
                if (isChecked) {
                    checkBoxes.filter { it != button }.forEach { it.isChecked = false }
                }
            }
        }

        binding.btnWithdraw.setOnClickListener {
            handleWithdrawClick()
        }
    }

    private fun handleWithdrawClick() {
        val selectedReasons = getSelectedReasons()
        val inputText = binding.textContent.text.toString().trim()

        val finalReason = when {
            // 기타가 선택된 경우 → 반드시 EditText 값 필요
            cbEtc.isChecked -> {
                if (inputText.isBlank()) {
                    Toast.makeText(requireContext(), "기타 사유를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }
                inputText
            }
            // 기타 외의 체크박스 선택
            selectedReasons.isNotEmpty() -> selectedReasons.joinToString(", ")
            else -> ""
        }

        if (finalReason.isBlank()) {
            Toast.makeText(requireContext(), "탈퇴 사유를 선택하거나 작성해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        settingViewModel.deleteUser(
            reason = finalReason,
            onSuccess = { msg ->
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), SocialLoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            },
            onFail = {
                Toast.makeText(requireContext(), "탈퇴 실패", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun getSelectedReasons(): List<String> =
        checkBoxes
            .filter { it.isChecked && it != cbEtc }
            .map { it.text.toString() }

}
