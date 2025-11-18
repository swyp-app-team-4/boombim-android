package com.example.swift.view.main.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMapBinding
import com.example.domain.model.CongestionData
import com.example.domain.model.MapTabType
import com.example.domain.model.MemberPlaceData
import com.example.swift.util.*
import com.example.swift.view.dialog.LoadingAlertProvider
import com.example.swift.view.main.map.adapter.NearByAdapter
import com.example.swift.view.main.map.helper.BottomSheetHelper
import com.example.swift.view.main.map.helper.MapHelper
import com.example.swift.view.main.map.helper.MarkerBitmapCache
import com.example.swift.viewmodel.MapViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : MapBaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate) {

    private var isFirstMove = true
    private var selectedTab: MapTabType = MapTabType.OFFICIAL

    private var lastCameraPosition: LatLng? = null
    private var lastZoomLevel: Int? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    override lateinit var markerBitmapCache: MarkerBitmapCache

    private lateinit var mapHelper: MapHelper
    private lateinit var bottomSheetHelper: BottomSheetHelper
    private lateinit var nearByAdapter: NearByAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        markerBitmapCache = MarkerBitmapCache(resources)
        bottomSheetHelper = BottomSheetHelper()
        bottomSheetHelper.initBottomSheet(binding.nearbyBottomSheet.root)

        selectedTab = MapTabType.OFFICIAL
        binding.textNotice.isSelected = true
        binding.textEvent.isSelected = false

        binding.textNotice.setOnClickListener {
            selectedTab = MapTabType.OFFICIAL
            binding.textNotice.isSelected = true
            binding.textEvent.isSelected = false
            refreshMarkers()
        }

        binding.textEvent.setOnClickListener {
            selectedTab = MapTabType.MEMBER
            binding.textNotice.isSelected = false
            binding.textEvent.isSelected = true
            refreshMarkers()
        }

        binding.buttonCurrentLocation.setOnClickListener {
            lifecycleScope.launch { moveCameraToCurrentLocation() }
        }

        binding.searchView.setOnClickListener {
           findNavController().navigate(R.id.kakaoSearchFragment)
        }


        setupMapView()

        initNearBy()


    }

    private fun setupMapView() {
        mapView = binding.mapView

        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {}
                override fun onMapError(p0: Exception?) {}
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaomap: KakaoMap) {
                    kakaoMap = kakaomap
                    mapHelper = MapHelper(kakaomap, markerBitmapCache)
                    setupCameraMoveListener()
                    observePlaces()

                    kakaoMap?.setOnLabelClickListener { _, _, label ->
                        when (val tag = label.tag) {
                            is CongestionData -> bottomSheetHelper.showOfficialPlaceSheet(tag, parentFragmentManager)
                            is MemberPlaceData.Place -> bottomSheetHelper.showMemberPlaceSheet(tag, parentFragmentManager)
                        }
                    }

                    handleSearchResult()
                }
            }
        )
    }

    private fun handleSearchResult() {
        val x = arguments?.getString("x")
        val y = arguments?.getString("y")

        Log.d("MapFragment", "📦 handleSearchResult() 호출됨 x=$x, y=$y")

        val xDouble = x?.toDoubleOrNull()
        val yDouble = y?.toDoubleOrNull()

        if (xDouble != null && yDouble != null) {
            lifecycleScope.launch {
                Log.d("MapFragment", "🎯 카메라 이동 시도: x=$xDouble, y=$yDouble (kakaoMap=${kakaoMap != null})")

                kakaoMap?.let {
                    val targetPosition = LatLng.from(yDouble, xDouble)
                    it.moveCamera(CameraUpdateFactory.newCenterPosition(targetPosition))
                    Log.d("MapFragment", "✅ 카메라 이동 완료")
                }
            }

            arguments?.clear()
        } else {
            lifecycleScope.launch {
                moveCameraToCurrentLocation()
            }
        }
    }

    private fun refreshMarkers() {
        kakaoMap ?: return
        when (selectedTab) {
            MapTabType.OFFICIAL -> mapHelper.addOfficialMarkers(mapViewModel.viewPortPlaceList.value)
            MapTabType.MEMBER -> mapHelper.addMemberMarkers(mapViewModel.memberPlaceList.value)
        }
    }

    private fun setupCameraMoveListener() {
        kakaoMap?.setOnCameraMoveEndListener { map, _, _ ->
            if (isFirstMove) {
                isFirstMove = false
                return@setOnCameraMoveEndListener
            }

            val cameraPos = map.cameraPosition ?: return@setOnCameraMoveEndListener
            val currentCamera = LatLng.from(cameraPos.position.latitude, cameraPos.position.longitude)
            val currentZoom = cameraPos.zoomLevel

            if (lastCameraPosition == currentCamera && lastZoomLevel == currentZoom) return@setOnCameraMoveEndListener
            lastCameraPosition = currentCamera
            lastZoomLevel = currentZoom

            val (southWest, northEast) = mapHelper.calculateViewRect(currentCamera, currentZoom, mapView.width, mapView.height)

            lifecycleScope.launch {
                LocationUtils.requestSingleUpdate(fusedLocationClient)?.let { location ->
                    when (selectedTab) {
                        MapTabType.OFFICIAL -> mapViewModel.fetchViewPortList(
                            topLeftLongitude = southWest.longitude,
                            topLeftLatitude = northEast.latitude,
                            bottomRightLongitude = northEast.longitude,
                            bottomRightLatitude = southWest.latitude,
                            memberLongitude = location.longitude,
                            memberLatitude = location.latitude,
                            zoomLevel = currentZoom
                        )
                        MapTabType.MEMBER -> mapViewModel.fetchMemberPlaceList(
                            topLeftLongitude = southWest.longitude,
                            topLeftLatitude = northEast.latitude,
                            bottomRightLongitude = northEast.longitude,
                            bottomRightLatitude = southWest.latitude,
                            memberLongitude = location.longitude,
                            memberLatitude = location.latitude,
                            zoomLevel = currentZoom
                        )
                    }
                }
            }
        }
    }

    private fun observePlaces() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapViewModel.viewPortPlaceList.collect { if (selectedTab == MapTabType.OFFICIAL) refreshMarkers() }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapViewModel.memberPlaceList.collect { if (selectedTab == MapTabType.MEMBER) refreshMarkers() }
            }
        }
    }

    private suspend fun moveCameraToCurrentLocation() {
        LocationUtils.getLastKnownLocation(requireContext(), fusedLocationClient)?.let {
            kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(it.latitude, it.longitude)))
        }
    }

    private fun initNearBy() {
        loadingAlertProvider.startLoading()
        val bottomSheetBinding = binding.nearbyBottomSheet

        bottomSheetBinding.recycle.layoutManager = LinearLayoutManager(requireContext())
        nearByAdapter = NearByAdapter(
            onItemClick = {
                findNavController().navigate(
                    R.id.homePlaceDetailFragment,
                    Bundle().apply {
                        putInt("placeId", it.officialPlaceId)
                    }
                )
            }
        )
        bottomSheetBinding.recycle.adapter = nearByAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapViewModel.viewPortPlaceList.collect { list ->
                    loadingAlertProvider.endLoading()
                    nearByAdapter.updateItems(list)
                }
            }
        }
    }
}
