package com.example.swift.view.main

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMapBinding
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextStyle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView : MapView
    private var kakaoMap : KakaoMap? = null

    private val placeList = listOf(
        Triple("강남역", 37.4979, 127.0276),
        Triple("서울역", 37.5547, 126.9706)
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showMapView()



    }

    private fun showMapView(){

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
                // 정상적으로 인증이 완료되었을 때 호출
                kakaoMap = kakaomap
                addMarkers()

                kakaoMap!!.setOnCameraMoveEndListener { kakaoMap, position, gestureType ->
                    Log.d(
                        "MapFragment", "Camera moved to: ${position.position.longitude}, ${position.position.latitude}"
                    )
                }
            }
        })
    }

    private fun addMarkers() {
        kakaoMap?.let { map ->
            val layer = map.labelManager?.layer ?: return

            val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_star)

            placeList.forEach { (name, lat, lng) ->
                val position = LatLng.from(lat, lng)

                // 아이콘 스타일
                val iconStyle = LabelStyle.from(markerBitmap)

                // 텍스트 스타일
                val textStyle = LabelTextStyle.from(
                    30,
                    requireContext().getColor(R.color.gray_scale_9)
                )
                val textLabelStyle = LabelStyle.from(textStyle)

                // 아이콘 라벨 옵션
                val iconOptions = LabelOptions.from(position)
                    .setStyles(iconStyle)
                    .setTexts(name) // 텍스트 없이 아이콘만

                // 텍스트 라벨 옵션 (위치 약간 아래로)
                val textPosition = LatLng.from(lat - 0.0003, lng)
                val textOptions = LabelOptions.from(textPosition)
                    .setStyles(textLabelStyle)
                    .setTexts(name)

                // 레이어에 각각 추가
                layer.addLabel(iconOptions)
//                layer.addLabel(textOptions)
            }

            // 첫 번째 위치로 카메라 이동
            val first = placeList.first()
            map.moveCamera(
                com.kakao.vectormap.camera.CameraUpdateFactory.newCenterPosition(
                    LatLng.from(first.second, first.third)
                )
            )
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}