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
import kotlinx.coroutines.launch

class AlertFragment : Fragment() {
    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!
    private val settingViewModel: SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconBack.setOnClickListener {
            findNavController().navigate(
                R.id.settingFragment, null,
                navOptions {
                    popUpTo(R.id.alertFragment) {
                        inclusive = true
                    }
                }
            )
        }

//       lifecycleScope.launch {
//           binding.alertToggle.setOnClickListener {
//               settingViewModel.updateNotificationAllowed()
//           }
//       }

        lifecycleScope.launch {
            settingViewModel.notificationAllowed.collect { allowed ->
                binding.alertToggle.isChecked = allowed
            }
        }

        binding.alertToggle.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.setNotificationAllowed(isChecked)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}