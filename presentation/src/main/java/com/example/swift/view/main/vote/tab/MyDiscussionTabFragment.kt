package com.example.swift.view.main.vote.tab

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.databinding.FragmentMyDiscussionTabBinding
import com.example.domain.model.SortType
import com.example.domain.model.TabType
import com.example.swift.view.dialog.EndVoteDialog
import com.example.swift.view.main.vote.adapter.MyVoteAdapter
import com.example.swift.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.swift.view.main.vote.VoteBaseFragment

@AndroidEntryPoint
class MyDiscussionTabFragment : VoteBaseFragment<FragmentMyDiscussionTabBinding>(
    FragmentMyDiscussionTabBinding::inflate
) {
    private val voteViewModel: VoteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 선택된 탭 설정
        setSelectedTab(binding.textAll)

        voteViewModel.setTabFilter(TabType.ALL)

        initTabClicks()

        initMyVoteList()

        binding.sortSelector.setOnClickListener { view ->
            val popup = PopupMenu(requireContext(), view)
            popup.menu.add("최신순")
            popup.menu.add("오래된순")

            popup.setOnMenuItemClickListener { item ->
                binding.sortText.text = item.title
                when (item.title) {
                    "최신순" -> voteViewModel.setSortType(SortType.LATEST)
                    "오래된순" -> voteViewModel.setSortType(SortType.OLDEST)
                }
                true
            }
            popup.show()
        }

    }

    private fun setSelectedTab(selected: TextView) {
        val allTabs = listOf(binding.textAll, binding.textFinished, binding.textProgress)
        allTabs.forEach { it.isSelected = (it == selected) }
    }

    private fun initTabClicks() = with(binding) {
        textAll.setOnClickListener {
            setSelectedTab(textAll)
            voteViewModel.setTabFilter(TabType.ALL)
        }
        textFinished.setOnClickListener {
            setSelectedTab(textFinished)
            voteViewModel.setTabFilter(TabType.END)
        }
        textProgress.setOnClickListener {
            setSelectedTab(textProgress)
            voteViewModel.setTabFilter(TabType.PROGRESS)
        }
    }

    private fun initMyVoteList() = with(binding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                voteViewModel.filteredVoteList.collect { myVoteList ->
                    val adapter = MyVoteAdapter(
                        myVoteList,
                        onBtnClick = { myVoteItem ->
                            EndVoteDialog(
                                onConfirm = {
                                    showLoading() // BaseViewBindingFragment에서 제공
                                    voteViewModel.patchVote(
                                        voteId = myVoteItem.voteId,
                                        onSuccess = { successMessage ->
                                            hideLoading()
                                            Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()
                                        },
                                        onFail = { errorMessage ->
                                            hideLoading()
                                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            ).show(parentFragmentManager, "EndVoteDialog")
                        }
                    )
                    recycleMyVote.layoutManager = LinearLayoutManager(requireContext())
                    recycleMyVote.adapter = adapter
                }
            }
        }
    }
}
