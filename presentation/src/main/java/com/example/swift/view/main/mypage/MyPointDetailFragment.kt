package com.example.swift.view.main.mypage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.boombim.android.databinding.FragmentMyPageBinding
import com.boombim.android.databinding.FragmentMyPointDetailBinding
import kotlinx.coroutines.launch

class MyPointDetailFragment : MyPageBaseFragment<FragmentMyPointDetailBinding>(
    FragmentMyPointDetailBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            myPageViewModel.myPoint.collect { point ->
                binding.textPoint.text = "${point}"
            }
        }



    }

}