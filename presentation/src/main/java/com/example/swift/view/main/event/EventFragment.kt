package com.example.swift.view.main.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.boombim.android.databinding.FragmentEventBinding
import com.boombim.android.databinding.FragmentMyPageBinding
import com.example.swift.view.main.mypage.MyPageBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class EventFragment  : MyPageBaseFragment<FragmentEventBinding>(
    FragmentEventBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       lifecycleScope.launch {
              myPageViewModel.eventInfo.collect { eventInfo ->
                eventInfo.let {
                    Log.d("EventFragment", "eventInfo: $it")
                     binding.textCurrentPoint.text = "${it.memberPoint}point"
                     binding.textCurrentTickets.text = "${it.currentTicket}장"
                }
              }
       }
    }
}