package com.example.swift

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BoomBimApp : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "15beddeb998ebfb5407b1501bf0b2259")
        KakaoMapSdk.init(this, "15beddeb998ebfb5407b1501bf0b2259")

        NaverIdLoginSDK.initialize(
            this,
            "WFRKsx3t7gMMESxx7Vth",        // 네이버 개발자 센터에서 발급
            "T3SCqoevVy",    // 네이버 개발자 센터에서 발급
            "붐빔"          // 사용자에게 보여질 앱 이름
        )

    }
}