package com.example.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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

        private val SAVED_FCM_TOKEN = stringPreferencesKey("saved_fcm_token")

        private val FIRST_LAUNCH = booleanPreferencesKey("first_launch")

        private val NOTIFICATION_ALLOWED = booleanPreferencesKey("notification_allowed")
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

    /**
     * 첫 실행인지 확인 한다. 값이 없으면 true로 준다
     */
    fun getIsFirstLaunch(): Flow<Boolean> {
        return appDataStore.data.map { preferences ->
            preferences[FIRST_LAUNCH] ?: true
        }
    }

    /**
     * 첫 앱 실행이 끝난 경우 구분
     */
    suspend fun setNotFirstLaunch() {
        appDataStore.edit { preferences ->
            preferences[FIRST_LAUNCH] = false
        }
    }

    suspend fun clearAllUserData() {
        appDataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(SAVED_FCM_TOKEN)
        }
    }

    suspend fun setSavedFcmToken(
        token: String?
    ) {
        appDataStore.edit { preferences ->
            if (token == null) {
                preferences.remove(SAVED_FCM_TOKEN)
            } else {
                preferences[SAVED_FCM_TOKEN] = token
            }
        }
    }

    fun getSavedFcmToken(): Flow<String?> {
        return appDataStore.data.map { preferences ->
            preferences[SAVED_FCM_TOKEN]
        }
    }

    suspend fun setNotificationAllowed(
        allowed: Boolean
    ) {
        appDataStore.edit { preferences ->
            preferences[NOTIFICATION_ALLOWED] = allowed
        }
    }

    fun getNotificationAllowed(): Flow<Boolean> {
        return appDataStore.data.map { preferences ->
            preferences[NOTIFICATION_ALLOWED] ?: false //따로 설정하지 않은 경우 허용 처리로 본다.
        }
    }
}