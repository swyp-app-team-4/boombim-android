package com.example.swift.view.main.discussion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.databinding.FragmentMyDiscussionTabBinding
import com.example.domain.model.TabType
import com.example.swift.view.main.discussion.adapter.MyVoteAdapter
import com.example.swift.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyDiscussionTabFragment : Fragment() {
    private var _binding: FragmentMyDiscussionTabBinding? = null
    private val binding get() = _binding!!
    private val voteViewModel: VoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyDiscussionTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 선택된 탭 설정
        setSelectedTab(binding.textAll)
        voteViewModel.setTabFilter(TabType.ALL)
        initTabClicks()

        initMyVoteList()

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


    private fun initMyVoteList() = with(binding){
        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                voteViewModel.filteredVoteList.collect { myVoteList ->
                   val adapter = MyVoteAdapter(myVoteList)
                    recycleMyVote.layoutManager = LinearLayoutManager(requireContext())
                    recycleMyVote.adapter = adapter
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}