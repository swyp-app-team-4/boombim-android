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
import com.boombim.android.databinding.FragmentMyPageVoteTabBinding
import com.example.swift.view.main.mypage.adapter.MyPageVoteSectionAdapter
import com.example.swift.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch

class MyPageVoteTabFragment : Fragment() {
    private var _binding: FragmentMyPageVoteTabBinding? = null
    private val binding get() = _binding!!
    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyPageVoteTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMyAnswer()

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

    private fun initMyAnswer() = with(binding){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                myPageViewModel.myAnswer.collect { list ->
                    if(list.isEmpty()){
                        recyclerVoteSection.visibility = View.GONE
                        addPlaceLayout.visibility = View.VISIBLE
                    }else{
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