package com.example.swift.view.main

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMapBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelTextStyle
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.toDegrees
import kotlin.math.cos
import kotlin.math.pow

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

    private val EARTH_RADIUS = 6378137.0

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

                    val cameraPosition = kakaoMap.cameraPosition
                    val centerLatLng = cameraPosition!!.position.latitude
                        .let { LatLng.from(it, cameraPosition.position.longitude) }
                    val zoomLevel = cameraPosition.zoomLevel

                    val mapWidthPx = mapView.width
                    val mapHeightPx = mapView.height

                    val (southWest, northEast) = calculateViewRect(centerLatLng, zoomLevel, mapWidthPx, mapHeightPx)

                    Log.d("MapFragment", "카메라 이동 위치: ${position.position.longitude}, ${position.position.latitude}")
                    Log.d("MapFragment", "남서 (SouthWest): ${southWest.latitude}, ${southWest.longitude}")
                    Log.d("MapFragment", "남서 (SouthWest): ${southWest.latitude}, ${southWest.longitude}")
                    Log.d("MapFragment", "북동 (NorthEast): ${northEast.latitude}, ${northEast.longitude}")

                    val southEast = LatLng.from(southWest.latitude, northEast.longitude)
                    val northWest = LatLng.from(northEast.latitude, southWest.longitude)

                    Log.d("MapFragment", "남동 (SouthEast): ${southEast.latitude}, ${southEast.longitude}")
                    Log.d("MapFragment", "북서 (NorthWest): ${northWest.latitude}, ${northWest.longitude}")
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

    private fun calculateViewRect(centerLatLng: LatLng, zoomLevel: Int, mapWidthPx: Int, mapHeightPx: Int): Pair<LatLng, LatLng> {
        // 한 픽셀당 미터 단위 계산 (대략)
        val metersPerPixel = 156543.03392 * cos(Math.toRadians(centerLatLng.latitude)) / 2.0.pow(zoomLevel)

        val halfWidthMeters = mapWidthPx / 2 * metersPerPixel
        val halfHeightMeters = mapHeightPx / 2 * metersPerPixel

        val latDiff = toDegrees(halfHeightMeters / EARTH_RADIUS)
        val lngDiff = toDegrees(halfWidthMeters / (EARTH_RADIUS * cos(Math.toRadians(centerLatLng.latitude))))

        val southWest = LatLng.from(centerLatLng.latitude - latDiff, centerLatLng.longitude - lngDiff)
        val northEast = LatLng.from(centerLatLng.latitude + latDiff, centerLatLng.longitude + lngDiff)

        return Pair(southWest, northEast)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}