package com.example.swift.view.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.databinding.FragmentMyPageBinding
import com.boombim.android.databinding.FragmentMyPointDetailBinding
import com.example.domain.model.PointFilterType
import com.example.domain.model.PointHistory
import com.example.swift.view.dialog.BuyTicketCompleteDialog
import com.example.swift.view.dialog.NoMoreAttemptsDialog
import com.example.swift.view.dialog.NotEnoughPointDialog
import com.example.swift.view.main.mypage.adapter.PointHistoryAdapter
import kotlinx.coroutines.launch

class MyPointDetailFragment : MyPageBaseFragment<FragmentMyPointDetailBinding>(
    FragmentMyPointDetailBinding::inflate
) {
    private lateinit var pointHistoryAdapter: PointHistoryAdapter
    private var currentFilter: PointFilterType = PointFilterType.ALL

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            myPageViewModel.myPoint.collect { point ->
                binding.textPoint.text = point.toString()
            }
        }

        setupBuyTicketButton()
        initRecyclerView()
        observePointHistory()
        setupTabs()

    }

    private fun setupBuyTicketButton() {
        binding.buttonEventApply.setOnClickListener {
            myPageViewModel.buyTicket(
                onSuccess = { showBuyTicketDialog() },
                onFail = { handleBuyTicketFailure(it) }
            )
        }
    }

    private fun applyFilter(list: List<PointHistory>) {
        val filtered = when (currentFilter) {
            PointFilterType.ALL -> list
            PointFilterType.EARN -> list.filter { it.pointAction == "EARN" }
            PointFilterType.USE -> list.filter { it.pointAction == "USE" }
        }

        pointHistoryAdapter.submitList(filtered)
    }

    private fun setupTabs() {

        binding.textAll.isSelected = true

        binding.textAll.setOnClickListener {
            currentFilter = PointFilterType.ALL
            updateTabUI()
            myPageViewModel.pointList.value.let { applyFilter(it) }
        }

        binding.textGetHistory.setOnClickListener {
            currentFilter = PointFilterType.EARN
            updateTabUI()
            myPageViewModel.pointList.value.let { applyFilter(it) }
        }

        binding.textUseHistory.setOnClickListener {
            currentFilter = PointFilterType.USE
            updateTabUI()
            myPageViewModel.pointList.value.let { applyFilter(it) }
        }
    }

    private fun updateTabUI() {
        binding.textAll.isSelected = (currentFilter == PointFilterType.ALL)
        binding.textGetHistory.isSelected = (currentFilter == PointFilterType.EARN)
        binding.textUseHistory.isSelected = (currentFilter == PointFilterType.USE)
    }


    /**  리사이클러뷰 설정 */
    private fun initRecyclerView() {
        pointHistoryAdapter = PointHistoryAdapter()
        binding.recyclerPointHistory.adapter = pointHistoryAdapter
        binding.recyclerPointHistory.layoutManager = LinearLayoutManager(requireContext())   // ✅ 이것!
    }


    /**  포인트 히스토리 리스트 반영 */
    private fun observePointHistory() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myPageViewModel.pointList.collect {
                    pointHistoryAdapter.submitList(it)
                }
            }
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