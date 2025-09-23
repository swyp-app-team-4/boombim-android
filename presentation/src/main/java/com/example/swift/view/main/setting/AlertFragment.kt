package com.example.swift.view.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentAlertBinding
import com.boombim.android.databinding.FragmentMyProfileBinding
import com.example.swift.viewmodel.MyPageViewModel
import com.example.swift.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlertFragment :
    SettingBaseFragment<FragmentAlertBinding>(FragmentAlertBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconBack.setOnClickListener {
            findNavController().navigate(
                R.id.settingFragment, null,
                navOptions {
                    popUpTo(R.id.alertFragment) { inclusive = true }
                }
            )
        }

        collectOnStarted {
            settingViewModel.notificationAllowed.collect { allowed ->
                binding.alertToggle.isChecked = allowed
            }
        }

        binding.alertToggle.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.setNotificationAllowed(isChecked)
            settingViewModel.patchAlarm(onSuccess = {}, onFail = {})
        }
    }
}
