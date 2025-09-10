package com.example.swift.view.main.map

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMapBinding
import com.example.domain.model.CongestionData
import com.example.domain.model.Coordinate
import com.example.domain.model.MemberPlaceData
import com.example.swift.util.LocationUtils
import com.example.swift.util.MapUtil
import com.example.swift.view.main.map.adapter.NearByAdapter
import com.example.swift.viewmodel.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelTextStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Math.toDegrees
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel: MapViewModel by activityViewModels()
    private var isFirstMove = true

    private enum class TabType { OFFICIAL, MEMBER }
    private var selectedTab: TabType = TabType.OFFICIAL

    private var lastCameraPosition: LatLng? = null
    private var lastZoomLevel: Int? = null

    private lateinit var nearByAdapter: NearByAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    private val EARTH_RADIUS = 6378137.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        selectedTab = TabType.OFFICIAL
        binding.textNotice.isSelected = true
        binding.textEvent.isSelected = false

        with(binding) {
            textNotice.setOnClickListener {
                selectedTab = TabType.OFFICIAL
                textNotice.isSelected = true
                textEvent.isSelected = false
                refreshMarkers()
            }

            textEvent.setOnClickListener {
                selectedTab = TabType.MEMBER
                textNotice.isSelected = false
                textEvent.isSelected = true
                refreshMarkers()
            }
        }

        setupMapView()

        initBottomSheet()

        initNearBy()
    }

    private fun initNearBy() {
        val bottomSheetBinding = binding.nearbyBottomSheet

        bottomSheetBinding.recycle.layoutManager = LinearLayoutManager(requireContext())
        nearByAdapter = NearByAdapter(emptyList())
        bottomSheetBinding.recycle.adapter = nearByAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapViewModel.viewPortPlaceList.collect { list ->
                    nearByAdapter.updateItems(list)
                }
            }
        }
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
                    lifecycleScope.launch { moveCameraToCurrentLocation() }
                    setupCameraMoveListener()
                    observePlaces()

                    kakaoMap?.setOnLabelClickListener { _, _, label ->
                        when (val tag = label.tag) {
                            is CongestionData -> {
                                showBottomSheet(tag)
                            }
                            is MemberPlaceData.Place -> {
                                showMemberBottomSheet(tag)
                            }
                        }
                    }

                }
            }
        )
    }

    private fun refreshMarkers() {
        val layer = kakaoMap?.labelManager?.layer ?: return
        layer.removeAll() // 기존 마커 제거

        when (selectedTab) {
            TabType.OFFICIAL -> {
                val officialList = mapViewModel.viewPortPlaceList.value
                // 탭이 OFFICIAL이면 OFFICIAL 마커만 그리기
                addMarkersFromViewModel(officialList)
            }
            TabType.MEMBER -> {
                val memberList = mapViewModel.memberPlaceList.value
                // 탭이 MEMBER이면 MEMBER 마커만 그리기
                addMarkersFromMemberViewModel(memberList)
            }
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

            // 이전 카메라 위치와 동일하면 호출하지 않음
            if (lastCameraPosition == currentCamera && lastZoomLevel == currentZoom) return@setOnCameraMoveEndListener
            lastCameraPosition = currentCamera
            lastZoomLevel = currentZoom

            val (southWest, northEast) = calculateViewRect(currentCamera, currentZoom, mapView.width, mapView.height)

            lifecycleScope.launch {
                LocationUtils.requestSingleUpdate(fusedLocationClient)?.let { location ->
                    when (selectedTab) {
                        TabType.OFFICIAL -> {
                            mapViewModel.fetchViewPortList(
                                topLeftLongitude = southWest.longitude,
                                topLeftLatitude = northEast.latitude,
                                bottomRightLongitude = northEast.longitude,
                                bottomRightLatitude = southWest.latitude,
                                memberLongitude = location.longitude,
                                memberLatitude = location.latitude,
                                zoomLevel = currentZoom
                            )
                        }
                        TabType.MEMBER -> {
                            mapViewModel.fetchMemberPlaceList(
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
    }

    private fun addMarkersFromViewModel(congestionList: List<CongestionData>) {
        val layer = kakaoMap?.labelManager?.layer ?: return

        congestionList.forEach { place ->
            val position = LatLng.from(place.coordinate.latitude, place.coordinate.longitude)

            val markerResId = when (place.congestionLevelName) {
                "여유" -> R.drawable.image_green_pin
                "보통" -> R.drawable.image_blue_pin
                "약간 붐빔" -> R.drawable.image_yellow_pin
                else -> R.drawable.image_pink_pin
            }

            val markerBitmap = BitmapFactory.decodeResource(resources, markerResId)
            val iconOptions = LabelOptions.from(position)
                .setStyles(LabelStyle.from(markerBitmap))
                .setTag(place)

            layer.addLabel(iconOptions)
        }
    }

    private fun addMarkersFromMemberViewModel(congestionList: List<MemberPlaceData>) {
        val layer = kakaoMap?.labelManager?.layer ?: return

        // 클러스터와 플레이스를 분리
        val clusters = congestionList.filterIsInstance<MemberPlaceData.Cluster>()
        val places = congestionList.filterIsInstance<MemberPlaceData.Place>()

        val distanceThreshold = 0.0001 // 약 10m 정도 (좌표 단위: 위도/경도)

        // 1) 클러스터 먼저 표시
        clusters.forEach { cluster ->
            val position = LatLng.from(cluster.coordinate.latitude, cluster.coordinate.longitude)
            val markerBitmap = MapUtil.makeClusterIcon(cluster.clusterSize)

            val iconOptions = LabelOptions.from(position)
                .setStyles(LabelStyle.from(markerBitmap))
                .setTag(cluster)

            layer.addLabel(iconOptions)
        }

        // 2) 개별 PLACE 표시 (클러스터 좌표 근처는 제외)
        places.forEach { place ->
            val isInCluster = clusters.any { cluster ->
                val latDiff = cluster.coordinate.latitude - place.coordinate.latitude
                val lngDiff = cluster.coordinate.longitude - place.coordinate.longitude
                abs(latDiff) < distanceThreshold && abs(lngDiff) < distanceThreshold
            }

            if (isInCluster) return@forEach

            val position = LatLng.from(place.coordinate.latitude, place.coordinate.longitude)
            val markerResId = when (place.congestionLevelName) {
                "여유" -> R.drawable.image_green_pin
                "보통" -> R.drawable.image_blue_pin
                "약간 붐빔" -> R.drawable.image_yellow_pin
                else -> R.drawable.image_pink_pin
            }

            val markerBitmap = BitmapFactory.decodeResource(resources, markerResId)
            val iconOptions = LabelOptions.from(position)
                .setStyles(LabelStyle.from(markerBitmap))
                .setTag(place)

            layer.addLabel(iconOptions)
        }
    }




    private fun observePlaces() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapViewModel.viewPortPlaceList.collect {
                    if (selectedTab == TabType.OFFICIAL) {
                        refreshMarkers()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapViewModel.memberPlaceList.collect {
                    if (selectedTab == TabType.MEMBER) {
                        refreshMarkers()
                    }
                }
            }
        }
    }



    private suspend fun moveCameraToCurrentLocation() {
        LocationUtils.getLastKnownLocation(requireContext(), fusedLocationClient)?.let {
            kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(it.latitude, it.longitude)))
        }
    }

    private fun calculateViewRect(center: LatLng, zoomLevel: Int, widthPx: Int, heightPx: Int): Pair<LatLng, LatLng> {
        val metersPerPixel = 156543.03392 * cos(Math.toRadians(center.latitude)) / 2.0.pow(zoomLevel)
        val halfWidthMeters = widthPx / 2 * metersPerPixel
        val halfHeightMeters = heightPx / 2 * metersPerPixel

        val latDiff = toDegrees(halfHeightMeters / EARTH_RADIUS)
        val lngDiff = toDegrees(halfWidthMeters / (EARTH_RADIUS * cos(Math.toRadians(center.latitude))))

        val southWest = LatLng.from(center.latitude - latDiff, center.longitude - lngDiff)
        val northEast = LatLng.from(center.latitude + latDiff, center.longitude + lngDiff)

        return southWest to northEast
    }

    private fun showBottomSheet(place: CongestionData) {
        val bottomSheet = OfficialPlaceBottomSheetFragment(place)
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showMemberBottomSheet(place: MemberPlaceData.Place) {
        val bottomSheet = MemberPlaceBottomSheetFragment(place)
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    private fun initBottomSheet() {
        val bottomSheet = binding.nearbyBottomSheet.root
        val behavior = BottomSheetBehavior.from(bottomSheet)

        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior.isHideable = false
        behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.1).toInt()
        behavior.maxHeight = (resources.displayMetrics.heightPixels * 0.7).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
