package com.example.swift.view.main.map.helper

import com.example.domain.model.CongestionData
import com.example.domain.model.MemberPlaceData
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import java.lang.Math.toDegrees
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow

class MapHelper(
    private val kakaoMap: KakaoMap,
    private val markerBitmapCache: MarkerBitmapCache
) {
    private val EARTH_RADIUS = 6378137.0
    private val distanceThreshold = 0.0001

    fun addOfficialMarkers(congestionList: List<CongestionData>) {
        val layer = kakaoMap.labelManager?.layer ?: return
        layer.removeAll()

        congestionList.forEach { place ->
            val position = LatLng.from(place.coordinate.latitude, place.coordinate.longitude)
            val markerResId = when (place.congestionLevelName) {
                "여유" -> com.boombim.android.R.drawable.image_green_pin
                "보통" -> com.boombim.android.R.drawable.image_blue_pin
                "약간 붐빔" -> com.boombim.android.R.drawable.image_yellow_pin
                else -> com.boombim.android.R.drawable.image_pink_pin
            }

            val markerBitmap = markerBitmapCache.getMarkerBitmap(markerResId)
            val iconOptions = LabelOptions.from(position)
                .setStyles(LabelStyle.from(markerBitmap))
                .setTag(place)

            layer.addLabel(iconOptions)
        }
    }

    fun addMemberMarkers(congestionList: List<MemberPlaceData>) {
        val layer = kakaoMap.labelManager?.layer ?: return
        layer.removeAll()

        val clusters = congestionList.filterIsInstance<MemberPlaceData.Cluster>()
        val places = congestionList.filterIsInstance<MemberPlaceData.Place>()

        clusters.forEach { cluster ->
            val position = LatLng.from(cluster.coordinate.latitude, cluster.coordinate.longitude)
            val markerBitmap = markerBitmapCache.getClusterBitmap(cluster.clusterSize)

            val iconOptions = LabelOptions.from(position)
                .setStyles(LabelStyle.from(markerBitmap))
                .setTag(cluster)

            layer.addLabel(iconOptions)
        }

        places.forEach { place ->
            val isInCluster = clusters.any { cluster ->
                val latDiff = cluster.coordinate.latitude - place.coordinate.latitude
                val lngDiff = cluster.coordinate.longitude - place.coordinate.longitude
                abs(latDiff) < distanceThreshold && abs(lngDiff) < distanceThreshold
            }
            if (isInCluster) return@forEach

            val position = LatLng.from(place.coordinate.latitude, place.coordinate.longitude)
            val markerResId = when (place.congestionLevelName) {
                "여유" -> com.boombim.android.R.drawable.image_green_pin
                "보통" -> com.boombim.android.R.drawable.image_blue_pin
                "약간 붐빔" -> com.boombim.android.R.drawable.image_yellow_pin
                else -> com.boombim.android.R.drawable.image_pink_pin
            }

            val markerBitmap = markerBitmapCache.getMarkerBitmap(markerResId)
            val iconOptions = LabelOptions.from(position)
                .setStyles(LabelStyle.from(markerBitmap))
                .setTag(place)

            layer.addLabel(iconOptions)
        }
    }

    fun calculateViewRect(center: LatLng, zoomLevel: Int, widthPx: Int, heightPx: Int): Pair<LatLng, LatLng> {
        val metersPerPixel = 156543.03392 * cos(center.latitude * Math.PI / 180) / 2.0.pow(zoomLevel)
        val halfWidthMeters = widthPx / 2 * metersPerPixel
        val halfHeightMeters = heightPx / 2 * metersPerPixel

        val latDiff = toDegrees(halfHeightMeters / EARTH_RADIUS)
        val lngDiff = toDegrees(halfWidthMeters / (EARTH_RADIUS * cos(center.latitude * Math.PI / 180)))

        val southWest = LatLng.from(center.latitude - latDiff, center.longitude - lngDiff)
        val northEast = LatLng.from(center.latitude + latDiff, center.longitude + lngDiff)

        return southWest to northEast
    }
}
