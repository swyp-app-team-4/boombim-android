package com.example.swift.view.main.mypage.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.databinding.FragmentMyPageMyVoteTabBinding
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.PopularityDetail
import com.example.swift.view.main.mypage.adapter.MyPageVoteSectionAdapter
import com.example.swift.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch

class MyPageMyVoteTabFragment : Fragment() {
    private var _binding: FragmentMyPageMyVoteTabBinding? = null
    private val binding get() = _binding!!
    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyPageMyVoteTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMyQuestion()

        lifecycleScope.launch {
            myPageViewModel.refreshVoteList()
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            myPageViewModel.refreshVoteList()
        }
    }

    private fun initMyQuestion() = with(binding){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                myPageViewModel.myQuestion.collect { list ->
                    if(list.isEmpty()){
                        recyclerVoteSection.visibility = View.GONE
                        addPlaceLayout.visibility = View.VISIBLE
                    }else {
                        recyclerVoteSection.visibility = View.VISIBLE
                        addPlaceLayout.visibility = View.GONE

                        val adapter = MyPageVoteSectionAdapter(list)
                        binding.recyclerVoteSection.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            this.adapter = adapter
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}