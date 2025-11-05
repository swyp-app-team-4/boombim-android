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
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.FragmentHomeBinding
import com.example.domain.model.InterestsPlaceModel
import com.example.domain.model.PlaceBoomBimModel
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.domain.model.RegionNewsModel
import com.example.swift.util.LocationUtils
import com.example.swift.view.dialog.LoadingAlertProvider
import com.example.swift.view.main.home.adapter.InterestsPlaceAdapter
import com.example.swift.view.main.home.adapter.PlaceBoomBimAdapter
import com.example.swift.view.main.home.adapter.PlaceLessBoomBimAdapter
import com.example.swift.view.main.home.adapter.RegionNewsAdapter
import com.example.swift.viewmodel.FavoriteViewModel
import com.example.swift.viewmodel.HomeViewModel
import com.example.swift.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val loadingAlertProvider by lazy {
        LoadingAlertProvider(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchLessBoomBimPlace()

        fetchFcmToken()

        initRegionNewsViewPager()

        initLessBoomBimPlace()

        initInterestsPlace()

        initPlaceBoomBimList()

        binding.iconAlert.setOnClickListener {
            findNavController().navigate(R.id.notificationFragment)
        }

        binding.imageEventBanner.setOnClickListener {
            findNavController().navigate(R.id.eventFragment)
        }


    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            favoriteViewModel.fetchFavorites()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchLessBoomBimPlace(){
        lifecycleScope.launch {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            val location = LocationUtils.getLastKnownLocation(requireContext(), fusedLocationClient)
                ?: LocationUtils.requestSingleUpdate(fusedLocationClient)

            location?.let {
                homeViewModel.fetchLessBoomBimList(it.latitude, it.longitude)
            }
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

    private fun initLessBoomBimPlace() = with(binding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.lessBoomBimList.collect { list ->
                    val adapter = PlaceLessBoomBimAdapter(list)
                    val layoutManager = GridLayoutManager(
                        requireContext(),
                        2,
                        GridLayoutManager.HORIZONTAL,
                        false
                    )
                    recyclerLessBoomBim.layoutManager = layoutManager
                    recyclerLessBoomBim.adapter = adapter
                }
            }
        }
    }

    private fun initInterestsPlace() = with(binding) {
        val adapter = InterestsPlaceAdapter { item ->
            favoriteViewModel.deleteFavorite(
                memberPlaceId = item.favoriteId,
                placeType = item.placeType,
                onSuccess = {},
                onFail = {}
            )
        }
        recyclerInterestPlace.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        recyclerInterestPlace.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.favoriteList.collect { list ->
                    adapter.submitList(list)

                    if (list.isEmpty()) {
                        textInterestPlace.visibility = View.GONE
                        divider3.visibility = View.GONE
                    } else {
                        textInterestPlace.visibility = View.VISIBLE
                        divider3.visibility = View.VISIBLE
                    }
                }
            }
        }
    }



    private fun initPlaceBoomBimList() = with(binding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.boomBimList.collect { list ->
                    recyclerBoomBim.layoutManager = LinearLayoutManager(requireContext())
                    recyclerBoomBim.adapter = PlaceBoomBimAdapter(list)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}