package com.example.swift.view.main.makecongestion

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMakeCongestionBinding
import com.example.swift.util.LocationUtils
import com.example.swift.view.dialog.MakeCongestionSuccessDialog
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class MakeCongestionFragment :
    MakeCongestionBaseFragment<FragmentMakeCongestionBinding>(FragmentMakeCongestionBinding::inflate) {

    private var congestionLevel: Int? = null
    private var kakaoMap: KakaoMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placeName = arguments?.getString("placeName").orEmpty()

        setCurrentTime(binding.textTime)
        initCongestionIcons()
        setupSearchClick()

        arguments?.let { bundle ->
            val data = extractBundleData(bundle)
            setupMap(data)
            setupPlaceInfo(data)
            setupAiMessageButton(data)
        }

        binding.btnShare.setOnClickListener {
            shareCongestion(placeName)
        }
    }

    // ------------------------------
    // Bundle Data Model
    // ------------------------------

    private data class PlaceData(
        val placeName: String,
        val addressName: String?,
        val latitude: Double?,
        val longitude: Double?,
        val serverPlaceId: Int,
        val id: Int
    )

    /** 번들값 추출 */
    private fun extractBundleData(bundle: Bundle) = PlaceData(
        placeName = bundle.getString("placeName").orEmpty(),
        addressName = bundle.getString("addressName"),
        latitude = bundle.getString("y")?.toDoubleOrNull(),
        longitude = bundle.getString("x")?.toDoubleOrNull(),
        serverPlaceId = bundle.getString("serverPlaceId")?.toIntOrNull() ?: -1,
        id = bundle.getInt("id")
    )

    // ------------------------------
    // Setup Functions
    // ------------------------------

    private fun setupSearchClick() {
        binding.searchView.setOnClickListener {
            findNavController().navigate(R.id.congestionSearchFragment)
        }
    }

    private fun setupMap(data: PlaceData) {
        if (data.longitude != null && data.latitude != null) {
            showMapView(data.longitude, data.latitude)
        }
    }

    private fun setupPlaceInfo(data: PlaceData) {
        if (data.placeName.isNotEmpty() && !data.addressName.isNullOrEmpty()) {
            showPlaceInfo(data.placeName, data.addressName)
        } else {
            hidePlaceInfo()
        }
    }

    private fun setupAiMessageButton(data: PlaceData) {
        binding.btnMakeAi.setOnClickListener {
            val message = binding.textContent.text.toString()
            val levelName = getCongestionLevelName()

            homeViewModel.getClovaToken(
                data.id,
                onSuccess = { token ->
                    homeViewModel.makeAutoMessage(
                        aiAttemptToken = token,
                        memberPlaceId = data.id,
                        memberPlaceName = data.placeName,
                        congestionLevelName = levelName,
                        congestionMessage = message,
                        onSuccess = { generatedMessage ->
                            binding.textContent.setText(generatedMessage)
                            Toast.makeText(requireContext(), "AI 메시지 생성 완료", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = {
                            Toast.makeText(requireContext(), "AI 메시지 생성 실패", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onFailure = {
                    Toast.makeText(requireContext(), "AI 메시지 생성 실패", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    // ------------------------------
    // UI Helpers
    // ------------------------------

    private fun showPlaceInfo(placeName: String, addressName: String) = with(binding) {
        textPlaceName.text = placeName
        textPlaceAddress.text = addressName
        textPlaceName.show()
        textPlaceAddress.show()
        mapView.show()
    }

    private fun hidePlaceInfo() = with(binding) {
        textPlaceName.visibility = View.GONE
        textPlaceAddress.visibility = View.GONE
        mapView.visibility = View.GONE
    }

    private fun getCongestionLevelName() = when (congestionLevel) {
        1 -> "Calm"
        2 -> "Normal"
        3 -> "Slightly Busy"
        4 -> "Busy"
        else -> "Unknown"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCurrentTime(textView: TextView) {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd a h시 m분", Locale.KOREA)
        textView.text = now.format(formatter)
    }

    // ------------------------------
    // Map
    // ------------------------------

    private fun showMapView(x: Double, y: Double) {
        binding.mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {}
                override fun onMapError(e: Exception?) {
                    Log.e("KakaoMap", "Map error", e)
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    kakaoMap = map
                    addMarker(y, x)
                }
            }
        )
    }

    private fun addMarker(lat: Double, lng: Double) {
        kakaoMap?.labelManager?.layer?.let { layer ->
            val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_red_marker)
            val position = LatLng.from(lat, lng)
            val style = LabelStyle.from(markerBitmap)

            val options = LabelOptions.from(position).setStyles(style)
            layer.addLabel(options)

            kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(position))
        }
    }

    // ------------------------------
    // Congestion Icon Handling
    // ------------------------------

    private fun initCongestionIcons() = with(binding) {
        val icons = listOf(
            imgCalm to Pair(R.drawable.icon_calm_v2, 1),
            imgNormal to Pair(R.drawable.icon_normal_v2, 2),
            iconSlightlyBusy to Pair(R.drawable.icon_slightly_busy_v2, 3),
            iconBusy to Pair(R.drawable.icon_busy_v2, 4)
        )

        resetIconsToDefault()

        icons.forEach { (imageView, pair) ->
            val (selectedRes, levelId) = pair
            imageView.setOnClickListener {
                resetIconsToSelected()
                imageView.setImageResource(selectedRes)
                congestionLevel = levelId
            }
        }
    }

    private fun resetIconsToDefault() = with(binding) {
        imgCalm.setImageResource(R.drawable.icon_calm_v1)
        imgNormal.setImageResource(R.drawable.icon_normal_v1)
        iconSlightlyBusy.setImageResource(R.drawable.icon_slightly_busy_v1)
        iconBusy.setImageResource(R.drawable.icon_busy_v1)
    }

    private fun resetIconsToSelected() = with(binding) {
        imgCalm.setImageResource(R.drawable.icon_calm_v3)
        imgNormal.setImageResource(R.drawable.icon_normal_v3)
        iconSlightlyBusy.setImageResource(R.drawable.icon_slightly_busy_v3)
        iconBusy.setImageResource(R.drawable.icon_busy_v3)
    }

    // ------------------------------
    // 서버로 전송
    // ------------------------------

    private fun shareCongestion(placeName: String) {
        val placeId = arguments?.getString("serverPlaceId")?.toIntOrNull() ?: -1
        val level = congestionLevel ?: return
        val message = binding.textContent.text.toString()

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        lifecycleScope.launch {
            val location = LocationUtils.getLastKnownLocation(requireContext(), fusedLocationClient)
                ?: LocationUtils.requestSingleUpdate(fusedLocationClient)

            if (location != null) {
                makeCongestionViewModel.makeCongestion(
                    memberPlaceId = placeId,
                    congestionLevelId = level,
                    congestionMessage = message,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    onSuccess = {
                        makeCongestionViewModel.addMyActivity(placeName, level)
                        MakeCongestionSuccessDialog(placeName)
                            .show(parentFragmentManager, "MakeCongestionSuccessDialog")

                        navigateToHome()
                    },
                    onFailure = {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Log.e("MakeCongestion", "위치 정보를 가져오지 못했습니다.")
            }
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(
            R.id.mapFragment, null,
            navOptions {
                popUpTo(findNavController().graph.startDestinationId) { inclusive = true }
            }
        )
    }
}
