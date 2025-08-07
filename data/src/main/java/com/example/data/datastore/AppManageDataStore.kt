package com.example.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val APP_CONFIG_NAME = "swift_app_config.pb"
private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = APP_CONFIG_NAME)

@Singleton
class AppManageDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val appDataStore = context.appDataStore

    companion object{
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KAKAO_ACCESS_TOKEN = stringPreferencesKey("kakao_access_token")
        private val KAKAO_REFRESH_TOKEN = stringPreferencesKey("kakao_refresh_token")
        private val NAVER_ACCESS_TOKEN = stringPreferencesKey("naver_access_token")
        private val NAVER_REFRESH_TOKEN = stringPreferencesKey("naver_refresh_token")
    }

    suspend fun saveAccessToken(token: String) {
        appDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    suspend fun saveRefreshToken(token: String) {
        appDataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = token
        }
    }

    fun getAccessToken(): Flow<String?> {
        return appDataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }


    fun getRefreshToken(): Flow<String?> {
        return appDataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }
    }

    suspend fun saveKakaoAccessToken(token: String) {
        appDataStore.edit { preferences ->
            preferences[KAKAO_ACCESS_TOKEN] = token
        }
    }

    suspend fun saveNaverRefreshToken(token: String) {
        appDataStore.edit { preferences ->
            preferences[KAKAO_REFRESH_TOKEN] = token
        }
    }

    suspend fun saveNaverAccessToken(token: String) {
        appDataStore.edit { preferences ->
            preferences[NAVER_ACCESS_TOKEN] = token
        }
    }

    suspend fun saveKakaoRefreshToken(token: String) {
        appDataStore.edit { preferences ->
            preferences[NAVER_REFRESH_TOKEN] = token
        }
    }

    fun getKakaoAccessToken(): Flow<String?> {
        return appDataStore.data.map { preferences ->
            preferences[KAKAO_ACCESS_TOKEN]
        }
    }

    fun getNaverAccessToken(): Flow<String?> {
        return appDataStore.data.map { preferences ->
            preferences[NAVER_ACCESS_TOKEN]
        }
    }
}