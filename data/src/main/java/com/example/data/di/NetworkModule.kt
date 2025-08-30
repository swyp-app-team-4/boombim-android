package com.example.data.di

import android.content.Context
import com.example.data.datastore.AppManageDataStore
import com.example.data.network.auth.AuthApi
import com.example.data.network.home.HomeApi
import com.example.data.network.kakaosearch.KakaoLocalApi
import com.example.data.network.map.MapApi
import com.example.data.network.mypage.MyPageApi
import com.example.data.network.notification.NotificationApi
import com.example.data.network.vote.VoteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.boombim.p-e.kr/"
    private const val KAKAO_URL = "https://dapi.kakao.com/"


    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .cache(Cache(File(context.cacheDir, "http_cache"), 50L * 1024L * 1024L))
            .build()
    }

    // Retrofit 기본 팩토리
    private fun makeRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF-8".toMediaType()))
            .build()
    }

    // Auth Retrofit
    @AuthRetrofit
    @Provides
    fun provideAuthRetrofit(
        okHttpClient: OkHttpClient,
        appManageDataStore: AppManageDataStore,
        tokenAuthenticator: TokenAuthenticator
    ): Retrofit {
        val client = okHttpClient.newBuilder()
            .authenticator(tokenAuthenticator)
            .addInterceptor(AuthInterceptor(appManageDataStore))
            .addNetworkInterceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                if (chain.request().method == "GET") {
                    originalResponse.newBuilder()
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=60")
                        .build()
                } else {
                    originalResponse
                }
            }
            .build()

        return makeRetrofit(client, BASE_URL)
    }

    // NoAuth Retrofit
    @NoAuthRetrofit
    @Provides
    fun provideNoAuthRetrofit(okHttpClient: OkHttpClient): Retrofit =
        makeRetrofit(okHttpClient, BASE_URL)

    // Kakao Retrofit
    @KakaoRetrofit
    @Provides
    fun provideKakaoRetrofit(okHttpClient: OkHttpClient): Retrofit =
        makeRetrofit(okHttpClient, KAKAO_URL)

    // API 제공
    @Provides
    @Singleton
    fun provideAuthApi(@NoAuthRetrofit retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideNotificationApi(@AuthRetrofit retrofit: Retrofit): NotificationApi =
        retrofit.create(NotificationApi::class.java)

    @Provides
    @Singleton
    fun provideVoteApi(@AuthRetrofit retrofit: Retrofit): VoteApi =
        retrofit.create(VoteApi::class.java)

    @Provides
    @Singleton
    fun provideHomeApi(@AuthRetrofit retrofit: Retrofit): HomeApi =
        retrofit.create(HomeApi::class.java)

    @Provides
    @Singleton
    fun provideMyPageApi(@AuthRetrofit retrofit: Retrofit): MyPageApi =
        retrofit.create(MyPageApi::class.java)

    @Provides
    @Singleton
    fun provideMapApi(@AuthRetrofit retrofit: Retrofit): MapApi =
        retrofit.create(MapApi::class.java)


    @Provides
    @Singleton
    fun provideKakaoLocalApi(@KakaoRetrofit retrofit: Retrofit): KakaoLocalApi =
        retrofit.create(KakaoLocalApi::class.java)

}
