# Keep model classes used by Gson / Kotlin Serialization etc.
-keep class kotlinx.serialization.** { *; }
-keep class com.example.domain.model.** { *; }

# Retrofit 사용 시
-dontwarn okhttp3.**
-dontwarn okio.**

# Hilt 사용 시 (중요)
-keep class dagger.hilt.** { *; }
-keep interface dagger.hilt.** { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent {
    *;
}

# DTO 직렬화 모델 유지
-keepclassmembers class ** {
    @kotlinx.serialization.SerialName <fields>;
}

# Enum 유지
-keepclassmembers enum * { *; }

# AndroidX Navigation SafeArgs
-keep class * extends androidx.navigation.NavDirections { *; }

# Kakao Map SDK Keep Rules
-keep class com.kakao.** { *; }
-keep class com.kakao.vectormap.** { *; }
-keep class com.kakao.mapsdk.** { *; }

# JNI(native) 메서드와 연결된 클래스는 절대 난독화 금지
-keep class * extends java.lang.Exception
-keepclasseswithmembers class * {
    native <methods>;
}

# Reflection 사용 가능성 대비
-keepclassmembers class com.kakao.** {
    *;
}
