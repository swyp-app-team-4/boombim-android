package com.example.swift

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.boombim.android.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BoomBimApp : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)

        NaverIdLoginSDK.initialize(
            this,
            "WFRKsx3t7gMMESxx7Vth",        // 네이버 개발자 센터에서 발급
            "T3SCqoevVy",    // 네이버 개발자 센터에서 발급
            "붐빔"          // 사용자에게 보여질 앱 이름
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "fcm_default_channel",
                "parkhwan",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

    }
}