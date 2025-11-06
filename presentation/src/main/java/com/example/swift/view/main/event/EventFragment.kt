package com.example.swift.view.main.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.boombim.android.databinding.FragmentEventBinding
import com.boombim.android.databinding.FragmentMyPageBinding
import com.example.swift.view.dialog.BuyTicketCompleteDialog
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
                     binding.textCurrentPoint.text = "${it.memberPoint}point"
                     binding.textCurrentTickets.text = "${it.currentTicket}장"
                }
              }
       }

        binding.iconBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonBuyTicket.setOnClickListener {
            myPageViewModel.buyTicket(
                onSuccess = {
                    val dialog = BuyTicketCompleteDialog()
                    dialog.show(parentFragmentManager, "BuyTicketCompleteDialog")
                },
                onFail = { msg ->
                    if (msg.contains("-1003")) {
                        Toast.makeText(requireContext(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
                    } else if (msg.contains("-1002")) {
                        Toast.makeText(requireContext(), "이벤트 응모 가능 횟수를 초과했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            )

        }
    }
}