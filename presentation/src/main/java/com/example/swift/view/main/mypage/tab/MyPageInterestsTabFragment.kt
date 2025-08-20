package com.example.swift.view.main.mypage.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.boombim.android.databinding.FragmentMyPageInterestsTabBinding
import com.example.domain.model.InterestsPlaceModel
import com.example.swift.view.main.home.adapter.InterestsPlaceAdapter

class MyPageInterestsTabFragment : Fragment() {
    private var _binding: FragmentMyPageInterestsTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyPageInterestsTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInterestsPlace()

    }

    private fun initInterestsPlace() = with(binding) {
        val interestsList = listOf(
            InterestsPlaceModel("강남역", "10분 전 업데이트됨", "congestion"),
            InterestsPlaceModel("홍대입구", "5분 전 업데이트됨", "normal"),
            InterestsPlaceModel("광화문", "1시간 전 업데이트됨", "relaxed"),
            InterestsPlaceModel("광화문", "1시간 전 업데이트됨", "relaxed"),
            InterestsPlaceModel("광화문", "1시간 전 업데이트됨", "relaxed")

        )

        val adapter = InterestsPlaceAdapter(interestsList)
        recyclerInterestPlace.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false) // 세로형
        recyclerInterestPlace.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}