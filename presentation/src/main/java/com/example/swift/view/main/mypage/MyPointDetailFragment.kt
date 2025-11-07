package com.example.swift.view.main.mypage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.boombim.android.databinding.FragmentMyPageBinding
import com.boombim.android.databinding.FragmentMyPointDetailBinding
import com.example.swift.view.dialog.BuyTicketCompleteDialog
import com.example.swift.view.dialog.NoMoreAttemptsDialog
import com.example.swift.view.dialog.NotEnoughPointDialog
import kotlinx.coroutines.launch

class MyPointDetailFragment : MyPageBaseFragment<FragmentMyPointDetailBinding>(
    FragmentMyPointDetailBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            myPageViewModel.myPoint.collect { point ->
                binding.textPoint.text = point.toString()
            }
        }

        setupBuyTicketButton()

    }

    private fun setupBuyTicketButton() {
        binding.buttonEventApply.setOnClickListener {
            myPageViewModel.buyTicket(
                onSuccess = { showBuyTicketDialog() },
                onFail = { handleBuyTicketFailure(it) }
            )
        }
    }

    /** 응모권 구매 성공 다이얼로그 */
    private fun showBuyTicketDialog() {
        BuyTicketCompleteDialog().show(parentFragmentManager, "BuyTicketCompleteDialog")
    }

    /** 응모권 구매 실패 처리 */
    private fun handleBuyTicketFailure(msg: String) {
        when {
            msg.contains("-1003") -> {
                NotEnoughPointDialog().show(parentFragmentManager, "NotEnoughPointDialog")
            }
            msg.contains("-1002") -> {
                NoMoreAttemptsDialog().show(parentFragmentManager, "NoMoreAttemptsDialog")
            }
        }
    }

}