package com.example.swift.util

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Paint.Align
import android.graphics.Typeface
import android.graphics.BitmapFactory
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.camera.CameraUpdateFactory

object MapUtil {

    /**
     * 일반 마커 추가
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

    /**
     * 클러스터용 원형 아이콘 생성
     */
    fun makeClusterIcon(size: Int): Bitmap {
        val diameter = 200
        val bitmap = Bitmap.createBitmap(diameter, diameter, Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paintCircle = Paint().apply {
            isAntiAlias = true
            color = Color.parseColor("#80FFBABA")
        }

        val paintText = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            textSize = 40f
            textAlign = Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }

        // 배경 원 그리기
        canvas.drawCircle(
            (diameter / 2).toFloat(),
            (diameter / 2).toFloat(),
            (diameter / 2).toFloat(),
            paintCircle
        )

        // 텍스트 중앙 정렬
        val textY = (diameter / 2 - (paintText.descent() + paintText.ascent()) / 2)
        canvas.drawText(size.toString(), (diameter / 2).toFloat(), textY, paintText)

        return bitmap
    }
}
