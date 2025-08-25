package com.example.swift.view.main.map

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMapBinding
import com.example.domain.model.CongestionData
import com.example.swift.util.LocationUtils
import com.example.swift.viewmodel.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
import kotlin.math.cos
import kotlin.math.pow

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel: MapViewModel by activityViewModels()
    private var isFirstMove = true

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    private val EARTH_RADIUS = 6378137.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMapBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        setupMapView()
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

                    kakaoMap?.setOnLabelClickListener { kakaoMap, layer, label -> // 라벨 클릭 시 처리할 로직
                        val place = label.tag as CongestionData
                        showBottomSheet(place)
                    }
                }
            }
        )
    }

    private fun setupCameraMoveListener() {
        kakaoMap?.setOnCameraMoveEndListener { map, _, _ ->
            if (isFirstMove) {
                isFirstMove = false
                return@setOnCameraMoveEndListener
            }

            val cameraPos = map.cameraPosition ?: return@setOnCameraMoveEndListener
            val centerLatLng = LatLng.from(cameraPos.position.latitude, cameraPos.position.longitude)
            val (southWest, northEast) = calculateViewRect(centerLatLng, cameraPos.zoomLevel, mapView.width, mapView.height)

            lifecycleScope.launch {
                LocationUtils.requestSingleUpdate(fusedLocationClient)?.let { location ->
                    mapViewModel.fetchViewPortList(
                        topLeftLongitude = southWest.longitude,
                        topLeftLatitude = northEast.latitude,
                        bottomRightLongitude = northEast.longitude,
                        bottomRightLatitude = southWest.latitude,
                        memberLongitude = location.longitude,
                        memberLatitude = location.latitude
                    )
                }
            }
        }
    }

    private fun addMarkersFromViewModel(congestionList: List<CongestionData>) {
        val layer = kakaoMap?.labelManager?.layer ?: return

        congestionList.forEach { place ->
            val position = LatLng.from(place.coordinate.latitude, place.coordinate.longitude)
            val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_star)
            val iconOptions = LabelOptions.from(position)
                .setStyles(LabelStyle.from(markerBitmap))
                .setTag(place)

            val textPosition = LatLng.from(place.coordinate.latitude - 0.0003, place.coordinate.longitude)
            val textOptions = LabelOptions.from(textPosition)
                .setStyles(LabelStyle.from(LabelTextStyle.from(30, requireContext().getColor(R.color.gray_scale_9))))
                .setTexts(place.name)
                .setTag(place)

            layer.addLabel(iconOptions)
            layer.addLabel(textOptions)
        }
    }

    private fun observePlaces() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapViewModel.viewPortPlaceList.collect { addMarkersFromViewModel(it) }
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
        val bottomSheet = PlaceBottomSheetFragment(place)
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
