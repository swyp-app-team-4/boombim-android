package com.example.swift.view.main.makecongestion

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMakeCongestionBinding
import com.example.swift.util.LocationUtils
import com.example.swift.viewmodel.MakeCongestionViewModel
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MakeCongestionFragment : Fragment() {

    private var _binding: FragmentMakeCongestionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MakeCongestionViewModel by activityViewModels()

    private var selectedCongestionLevel: Int? = null
    private var kakaoMap: KakaoMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeCongestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCurrentTime(binding.textTime)
        handleArguments(arguments)
        initCongestionIcons()

        binding.searchView.setOnClickListener {
            findNavController().navigate(
                R.id.congestionSearchFragment)
        }

        binding.btnShare.setOnClickListener { shareCongestion() }
    }

    /** 번들 값 처리 */
    private fun handleArguments(bundle: Bundle?) {
        val placeName = bundle?.getString("placeName")
        val addressName = bundle?.getString("addressName")
        val longitude = bundle?.getString("x")?.toDoubleOrNull()
        val latitude = bundle?.getString("y")?.toDoubleOrNull()
        val serverPlaceId = bundle?.getString("serverPlaceId")?.toIntOrNull() ?: -1

        Log.d("MakeFragment", "serverPlaceId=$serverPlaceId")

        if (longitude != null && latitude != null) {
            showMapView(longitude, latitude)
        }

        if (!placeName.isNullOrEmpty() && !addressName.isNullOrEmpty()) {
            showPlaceInfo(placeName, addressName)
        } else {
            hidePlaceInfo()
        }
    }

    /** 장소 정보 표시 */
    private fun showPlaceInfo(placeName: String, addressName: String) = with(binding) {
        textPlaceName.text = placeName
        textPlaceAddress.text = addressName
        textPlaceName.show()
        textPlaceAddress.show()
        mapView.show()
    }

    private fun hidePlaceInfo() = with(binding) {
        textPlaceName.hide()
        textPlaceAddress.hide()
        mapView.hide()
    }

    /** 지도 표시 */
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

    private fun addMarker(x: Double, y: Double) {
        kakaoMap?.labelManager?.layer?.let { layer ->
            val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_red_marker)
            val position = LatLng.from(x, y)
            val iconStyle = LabelStyle.from(markerBitmap)

            val iconOptions = LabelOptions.from(position).setStyles(iconStyle)
            layer.addLabel(iconOptions)

            kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(position))
        }
    }

    /** 현재 시간 텍스트뷰에 세팅 */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCurrentTime(textView: TextView) {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd a h시 m분", Locale.KOREA)
        textView.text = now.format(formatter)
    }

    /** 혼잡도 아이콘 초기화 */
    private fun initCongestionIcons() = with(binding) {
        val icons = listOf(
            imgCalm to Pair(R.drawable.icon_calm_v2, 1),
            imgNormal to Pair(R.drawable.icon_normal_v2, 2),
            iconSlightlyBusy to Pair(R.drawable.icon_slightly_busy_v2, 3),
            iconBusy to Pair(R.drawable.icon_busy_v2, 4)
        )

        // 기본 v1 세팅
        imgCalm.setImageResource(R.drawable.icon_calm_v1)
        imgNormal.setImageResource(R.drawable.icon_normal_v1)
        iconSlightlyBusy.setImageResource(R.drawable.icon_slightly_busy_v1)
        iconBusy.setImageResource(R.drawable.icon_busy_v1)

        icons.forEach { (imageView, pair) ->
            val (selectedRes, levelId) = pair
            imageView.setOnClickListener {
                // 모두 v3으로 초기화
                imgCalm.setImageResource(R.drawable.icon_calm_v3)
                imgNormal.setImageResource(R.drawable.icon_normal_v3)
                iconSlightlyBusy.setImageResource(R.drawable.icon_slightly_busy_v3)
                iconBusy.setImageResource(R.drawable.icon_busy_v3)

                // 클릭된 아이콘 v2 + 선택 레벨 저장
                imageView.setImageResource(selectedRes)
                selectedCongestionLevel = levelId
            }
        }
    }

    /** 서버로 혼잡도 전송 */
    private fun shareCongestion() {
        val placeId = arguments?.getString("serverPlaceId")?.toIntOrNull() ?: -1
        val congestionLevelId = selectedCongestionLevel ?: return
        val message = binding.textContent.text.toString()

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        lifecycleScope.launch {
            val location = LocationUtils.getLastKnownLocation(requireContext(), fusedLocationClient)
                ?: LocationUtils.requestSingleUpdate(fusedLocationClient)

            if (location != null) {
                viewModel.makeCongestion(
                    memberPlaceId = placeId,
                    congestionLevelId = congestionLevelId,
                    congestionMessage = message,
                    latitude = location.latitude,
                    longitude = location.longitude,
//                    latitude = "37.50437663505579".toDouble(),
//                    longitude = "127.04897066287083".toDouble(),
                    onSuccess = { msg ->
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(
                            R.id.homeFragment,
                            null,
                            navOptions {
                                popUpTo(findNavController().graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        )

                    },
                    onFailure = { msg ->
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Log.e("MakeCongestion", "위치 정보를 가져오지 못했습니다.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** View 확장 함수 */
    private fun View.show() { visibility = View.VISIBLE }
    private fun View.hide() { visibility = View.GONE }
}
