package com.example.swift.util

import android.content.Context
import android.graphics.BitmapFactory
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.camera.CameraUpdateFactory

object MapUtil {

    /**
     * 카카오맵에 마커를 추가하는 유틸 함수
     *
     * @param context Context
     * @param kakaoMap KakaoMap 객체
     * @param latitude 위도
     * @param longitude 경도
     * @param markerResId 마커로 사용할 리소스 ID (예: R.drawable.icon_red_marker)
     * @param moveCamera 마커 위치로 카메라 이동 여부 (기본 true)
     */
    fun addMarker(
        context: Context,
        kakaoMap: KakaoMap?,
        latitude: Double,
        longitude: Double,
        markerResId: Int,
        moveCamera: Boolean = true
    ) {
        kakaoMap?.labelManager?.layer?.let { layer ->
            val markerBitmap = BitmapFactory.decodeResource(context.resources, markerResId)
            val position = LatLng.from(latitude, longitude)

            val iconOptions = LabelOptions.from(position)
                .setStyles(LabelStyle.from(markerBitmap))

            layer.addLabel(iconOptions)

            if (moveCamera) {
                kakaoMap.moveCamera(
                    CameraUpdateFactory.newCenterPosition(position)
                )
            }
        }
    }
}
