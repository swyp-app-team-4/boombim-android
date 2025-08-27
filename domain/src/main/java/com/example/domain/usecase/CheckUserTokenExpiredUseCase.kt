package com.example.domain.usecase

import android.util.Base64
import com.example.domain.model.TokenStatus
import com.example.domain.provider.TokenProvider
import org.json.JSONObject
import java.nio.charset.Charset
import javax.inject.Inject

/** * 현재 토큰이 있는지 확인 하고, * 있다면 만료되었는지 확인 한다. * @retrun 없거나,만료된경우 false , 아니면 true */
class CheckUserTokenExpiredUseCase @Inject constructor(private val tokenProvider: TokenProvider) {
    suspend operator fun invoke(): TokenStatus {
        val refreshToken: String? =
            tokenProvider.getRefreshToken()
        return if (refreshToken == null) {
            tokenProvider.clearAllUserData()
            TokenStatus.NONE
        } else {
            val exp = getJwtExpiration(refreshToken)
            val now = System.currentTimeMillis() / 1000
            if (exp == null) {
                TokenStatus.NONE
            } else {
                if (exp > now) {
                    TokenStatus.NOT_EXPIRED
                } else {
                    TokenStatus.EXPIRED
                }
            }
        }
    }

    private fun getJwtExpiration(token: String): Long? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null

            val payload = parts[1]
            val decodedBytes =
                Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            val json = JSONObject(String(decodedBytes, Charset.forName("UTF-8")))

            json.getLong("exp")
        } catch (e: Exception) {
            null
        }
    }
}