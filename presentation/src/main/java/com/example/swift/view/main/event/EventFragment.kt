package com.example.swift.view.main.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentEventBinding
import com.boombim.android.databinding.FragmentMyPageBinding
import com.example.swift.view.dialog.BuyTicketCompleteDialog
import com.example.swift.view.dialog.NoMoreAttemptsDialog
import com.example.swift.view.dialog.NotEnoughPointDialog
import com.example.swift.view.main.mypage.MyPageBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class EventFragment : MyPageBaseFragment<FragmentEventBinding>(
    FragmentEventBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectEventInfo()

        setupBackButton()

        setupAlertCongestionButton()

        setupBuyTicketButton()
    }

    /** 이벤트 정보 Flow 수집 */
    private fun collectEventInfo() {
        lifecycleScope.launch {
            myPageViewModel.eventInfo.collect { eventInfo ->
                binding.textCurrentPoint.text = "${eventInfo.memberPoint}point"
                binding.textCurrentTickets.text = "${eventInfo.currentTicket}장"
            }
        }
    }

    /** 뒤로가기 버튼 */
    private fun setupBackButton() {
        binding.iconBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /** 혼잡도 알리기 버튼 */
    private fun setupAlertCongestionButton() {
        binding.btnAlertCongestion.setOnClickListener {
            findNavController().navigate(
                R.id.makeCongestionFragment,
                null,
                navOptions {
                    popUpTo(R.id.eventFragment) { inclusive = true }
                }
            )
        }
    }

    /** 응모권 구매하기 버튼 */
    private fun setupBuyTicketButton() {
        binding.buttonBuyTicket.setOnClickListener {
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
