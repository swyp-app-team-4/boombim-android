package com.example.swift.view.main.map.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.boombim.android.R
import com.example.swift.util.MapUtil

class MarkerBitmapCache(private val resources: android.content.res.Resources) {

    private val markerCache: MutableMap<Int, Bitmap> = mutableMapOf(
        R.drawable.image_green_pin to BitmapFactory.decodeResource(resources, R.drawable.image_green_pin),
        R.drawable.image_blue_pin to BitmapFactory.decodeResource(resources, R.drawable.image_blue_pin),
        R.drawable.image_yellow_pin to BitmapFactory.decodeResource(resources, R.drawable.image_yellow_pin),
        R.drawable.image_pink_pin to BitmapFactory.decodeResource(resources, R.drawable.image_pink_pin),
        R.drawable.image_gray_pin to BitmapFactory.decodeResource(resources, R.drawable.image_gray_pin)
    )

    private val clusterCache: MutableMap<Int, Bitmap> = mutableMapOf()

    fun getMarkerBitmap(resId: Int): Bitmap {
        return markerCache[resId] ?: BitmapFactory.decodeResource(resources, resId).also {
            markerCache[resId] = it
        }
    }

    fun getClusterBitmap(size: Int): Bitmap {
        return clusterCache[size] ?: MapUtil.makeClusterIcon(size).also {
            clusterCache[size] = it
        }
    }

    fun clear() {
        markerCache.values.forEach { if (!it.isRecycled) it.recycle() }
        markerCache.clear()
        clusterCache.values.forEach { if (!it.isRecycled) it.recycle() }
        clusterCache.clear()
    }
}
