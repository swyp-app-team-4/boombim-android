plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.gms.google.services)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.boombim.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.boombim.android"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("androidx.fragment:fragment:1.8.0")

    //Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)

    //KaKao APi
    implementation("com.kakao.sdk:v2-user:2.21.0")  // 카카오톡 사용자 정보 API 모듈
    implementation ("com.kakao.maps.open:android:2.9.5") // 카카오 지도 API 모듈

    // 네이버 sdk
    implementation("com.navercorp.nid:oauth:5.10.0")

    //Dot 라이브러리
    implementation("com.tbuonomo:dotsindicator:5.1.0")

    implementation (libs.play.services.location)

    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.github.skydoves:balloon:1.6.12")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}