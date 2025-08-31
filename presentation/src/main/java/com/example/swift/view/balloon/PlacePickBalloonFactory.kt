package com.example.swift.view.balloon

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import com.boombim.android.R
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon

class PlacePickBalloonFactory : Balloon.Factory() {
    override fun create(context: Context, lifecycle: LifecycleOwner?): Balloon {
        return createBalloon(context) {
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)

            setText("내 위치 300m이내 장소만 올릴 수 있어요 !")
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setTextSize(12f)
            ResourcesCompat.getFont(context, R.font.pretend_regular)?.let { setTextTypeface(it) }

            setArrowSize(10)
            setArrowOrientation(ArrowOrientation.TOP)
            setArrowPosition(0.92f)

            setMarginTop(5)
            setMarginRight(15)
            setPaddingVertical(8)
            setPaddingHorizontal(10)

            setCornerRadius(8f)
            setBackgroundColor(ContextCompat.getColor(context, R.color.main_color))
            setBalloonAnimation(BalloonAnimation.OVERSHOOT)

            setLifecycleOwner(lifecycle)
        }
    }
}