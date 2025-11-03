package com.example.swift.view.main.event

import android.os.Bundle
import android.view.View
import com.boombim.android.databinding.FragmentEventBinding
import com.boombim.android.databinding.FragmentMyPageBinding
import com.example.swift.view.main.mypage.MyPageBaseFragment

class EventFragment  : MyPageBaseFragment<FragmentEventBinding>(
    FragmentEventBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}