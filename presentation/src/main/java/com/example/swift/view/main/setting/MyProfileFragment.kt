package com.example.swift.view.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentAlertBinding
import com.boombim.android.databinding.FragmentMyProfileBinding
import com.boombim.android.databinding.FragmentSettingBinding
import com.example.swift.view.dialog.LogoutDialog
import com.example.swift.viewmodel.MyPageViewModel
import com.example.swift.viewmodel.SettingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MyProfileFragment :
    SettingBaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            myPageViewModel.profile.collect() { profile ->
                if(profile.socialProvider == "KAKAO"){
                    binding.textSocialLogin.text = "카카오"
                    binding.imageProvider.setImageResource(R.drawable.icon_circle_kakao)
                } else {
                    binding.textSocialLogin.text = "네이버"
                    binding.imageProvider.setImageResource(R.drawable.icon_naver_circle)
                }
            }
        }


    }
}