package com.example.swift.view.main.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.FragmentHomeBinding
import com.example.domain.model.InterestsPlaceModel
import com.example.domain.model.PlaceBoomBimModel
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.domain.model.RegionNewsModel
import com.example.swift.view.main.home.adapter.InterestsPlaceAdapter
import com.example.swift.view.main.home.adapter.PlaceBoomBimAdapter
import com.example.swift.view.main.home.adapter.PlaceLessBoomBimAdapter
import com.example.swift.view.main.home.adapter.RegionNewsAdapter
import com.example.swift.viewmodel.HomeViewModel
import com.example.swift.viewmodel.MainViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchFcmToken()

        initRegionNewsViewPager()

        initLessBoomBimPlace()

        initInterestsPlace()

        initPlaceBoomBimList()

        binding.iconAlert.setOnClickListener {
            findNavController().navigate(R.id.notificationFragment)
        }
        binding.iconSearch.setOnClickListener {
            findNavController().navigate(R.id.homeSearchFragment)
        }

        binding.btnMakeVote.setOnClickListener {
            findNavController().navigate(R.id.makeCongestionFragment)
        }

    }

    private fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    mainViewModel.updateToken(token)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRegionNewsViewPager() = with(binding) {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.region.collect{ list ->
                    viewPagerRegionNews.adapter = RegionNewsAdapter(list)
                    dotsIndicator.attachTo(viewPagerRegionNews)
                }
            }
        }

    }

    private fun initLessBoomBimPlace() = with((binding)){
        val placeList = listOf(
            PlaceLessBoomBimModel("노들섬", "14분 전", "congestion"),    // 혼잡
            PlaceLessBoomBimModel("시청 광장", "20분 전", "normal"),      // 보통
            PlaceLessBoomBimModel("뚝섬 유원지", "5분 전", "relaxed"),     // 여유
            PlaceLessBoomBimModel("여의도 한강공원", "1시간 전", "congestion"),
            PlaceLessBoomBimModel("경복궁", "30분 전", "normal"),
            PlaceLessBoomBimModel("남산서울타워", "25분 전", "relaxed")
        )

        val adapter = PlaceLessBoomBimAdapter(placeList)
        val layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        recyclerLessBoomBim.layoutManager = layoutManager
        recyclerLessBoomBim.adapter = adapter
    }

    private fun initInterestsPlace() = with(binding) {
        val interestsList = listOf(
            InterestsPlaceModel("강남역", "10분 전 업데이트됨", "congestion"),
            InterestsPlaceModel("홍대입구", "5분 전 업데이트됨", "normal"),
            InterestsPlaceModel("광화문", "1시간 전 업데이트됨", "relaxed")
        )

        val adapter = InterestsPlaceAdapter(interestsList)
        recyclerInterestPlace.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false) // 세로형
        recyclerInterestPlace.adapter = adapter
    }

    private fun initPlaceBoomBimList() = with(binding) {
        val items = listOf(
            PlaceBoomBimModel("시청 광장", "강남역 1번출구", 2,"congestion", "14분전"),
            PlaceBoomBimModel("경복궁", "강남역 1번출구", 1,"normal", "14분전"),
            PlaceBoomBimModel("한강 공원", "강남역 1번출구", 3,"congestion", "14분전"),
            PlaceBoomBimModel("시청 광장", "강남역 1번출구", 4,"congestion", "14분전"),
            PlaceBoomBimModel("경복궁", "강남역 1번출구", 5,"normal", "14분전"),
        )
        val list = items.sortedBy { it.ranking }

        recyclerBoomBim.layoutManager = LinearLayoutManager(requireContext())
        recyclerBoomBim.adapter = PlaceBoomBimAdapter(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}