package com.example.swift.view.main.makecongestion

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMakeCongestionBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MakeCongestionFragment : Fragment() {
    private var _binding: FragmentMakeCongestionBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView : MapView
    private var kakaoMap : KakaoMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeCongestionBinding.inflate(inflater, container, false)

        initSearchViewClick()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCurrentTimeToTextView(binding.textTime)

        handleArguments(arguments)

        initCongestionIcons()
    }

    private fun initSearchViewClick() {
        binding.searchView.setOnClickListener {
            findNavController().navigate(R.id.congestionSearchFragment)
        }
    }

    private fun handleArguments(bundle: Bundle?) {
        val placeName = bundle?.getString("placeName")
        val addressName = bundle?.getString("addressName")
        val longitude = bundle?.getString("x")?.toDoubleOrNull()
        val latitude = bundle?.getString("y")?.toDoubleOrNull()

        if (longitude != null && latitude != null) {
            showMapView(longitude, latitude)
        }

        if (!placeName.isNullOrEmpty() && !addressName.isNullOrEmpty()) {
            showPlaceInfo(placeName, addressName)
        } else {
            hidePlaceInfo()
        }
    }


    private fun showPlaceInfo(placeName: String, addressName: String) {
        binding.textPlaceName.text = placeName
        binding.textPlaceAddress.text = addressName
        binding.textPlaceName.visibility = View.VISIBLE
        binding.textPlaceAddress.visibility = View.VISIBLE
        binding.mapView.visibility = View.VISIBLE
    }

    private fun hidePlaceInfo() {
        binding.textPlaceName.visibility = View.GONE
        binding.textPlaceAddress.visibility = View.GONE
        binding.mapView.visibility = View.GONE
    }

    private fun showMapView(x: Double, y: Double){

        mapView = binding.mapView

        mapView.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출

            }

            override fun onMapError(p0: Exception?) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출

            }
        }, object : KakaoMapReadyCallback(){
            override fun onMapReady(kakaomap: KakaoMap) {
                kakaoMap = kakaomap

                addMarker(y, x)

            }
        })
    }

    private fun addMarker(x: Double, y: Double){
        kakaoMap?.let { map ->
            val layer = map.labelManager?.layer ?: return

            val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_red_marker)
            val position = LatLng.from(x, y)
            val iconStyle = LabelStyle.from(markerBitmap)

            val iconOptions = LabelOptions.from(position)
                .setStyles(iconStyle)

            layer.addLabel(iconOptions)

            map.moveCamera(
                CameraUpdateFactory.newCenterPosition(
                    LatLng.from(x,y)
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setCurrentTimeToTextView(textView: TextView) {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd a h시 m분", Locale.KOREA)
        val formattedTime = now.format(formatter)
        textView.text = formattedTime
    }

    private fun initCongestionIcons() {
        val icons = listOf(
            binding.imgCalm to R.drawable.icon_calm_v2,
            binding.imgNormal to R.drawable.icon_normal_v2,
            binding.iconSlightlyBusy to R.drawable.icon_slightly_busy_v2,
            binding.iconBusy to R.drawable.icon_busy_v2
        )

        // 기본값(v1) 세팅
        binding.imgCalm.setImageResource(R.drawable.icon_calm_v1)
        binding.imgNormal.setImageResource(R.drawable.icon_normal_v1)
        binding.iconSlightlyBusy.setImageResource(R.drawable.icon_slightly_busy_v1)
        binding.iconBusy.setImageResource(R.drawable.icon_busy_v1)

        // 클릭 이벤트 등록
        icons.forEach { (imageView, selectedRes) ->
            imageView.setOnClickListener {
                // 먼저 모두 v3로 초기화
                binding.imgCalm.setImageResource(R.drawable.icon_calm_v3)
                binding.imgNormal.setImageResource(R.drawable.icon_normal_v3)
                binding.iconSlightlyBusy.setImageResource(R.drawable.icon_slightly_busy_v3)
                binding.iconBusy.setImageResource(R.drawable.icon_busy_v3)

                // 클릭된 아이콘은 v2로 설정
                imageView.setImageResource(selectedRes)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
