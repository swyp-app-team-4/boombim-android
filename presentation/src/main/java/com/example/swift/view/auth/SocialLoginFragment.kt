package com.example.swift.view.auth

import android.app.Activity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.boombim.android.R

import com.example.swift.viewmodel.AuthViewModel
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.boombim.android.databinding.FragmentSocialLoginBinding
import com.example.swift.view.MainActivity
import com.example.swift.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SocialLoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentSocialLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    private val mainViewModel: MainViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 5000

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                Log.d("SocialLoginActivity", NaverIdLoginSDK.getAccessToken().toString())
                Log.d("SocialLoginActivity", NaverIdLoginSDK.getRefreshToken().toString())
                handleNaverToken(
                    NaverIdLoginSDK.getAccessToken().toString(),
                    NaverIdLoginSDK.getRefreshToken().toString()
                )
            }

            Activity.RESULT_CANCELED -> {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("NaverLogin", "Error $errorCode: $errorDescription")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSocialLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKakaoLogin.setOnClickListener {
            kakaoLogin()
        }

        binding.btnNaverLogin.setOnClickListener {
            NaverIdLoginSDK.authenticate(this, launcher)
        }

        setTextColor()

        permissionCheck()
    }

    private fun kakaoLogin() {
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                UserApiClient.instance.loginWithKakaoAccount(this) { accountToken, accountError ->
                    if (accountToken != null) {
                        handleKakaoToken(accountToken.accessToken, accountToken.refreshToken)
                    } else {
                        Log.e("SocialLoginActivity", "카카오계정 로그인 실패", accountError)
                    }
                }
            } else if (token != null) {
                handleKakaoToken(token.accessToken, token.refreshToken)
            }
        }
    }

    private fun handleKakaoToken(accessToken: String, refreshToken: String) {
        authViewModel.socialLogin(
            accessToken = accessToken,
            refreshToken = refreshToken,
            provider = "kakao",
            onSuccess = { nameFlag ->
                if (nameFlag) {
                    Toast.makeText(this, "카카오 로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "카카오 로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("dest", "edit")
                    }
                    startActivity(intent)
                }
            },
            onFail = { msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun handleNaverToken(accessToken: String, refreshToken: String) {
        authViewModel.socialLogin(
            accessToken = accessToken,
            refreshToken = refreshToken,
            provider = "naver",
            onSuccess = { nameFlag ->
                if (nameFlag) {
                    Toast.makeText(this, "네이버 로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "네이버 로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("dest", "edit")
                    }
                    startActivity(intent)
                }
            },
            onFail = { msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setTextColor() = with(binding) {
        val full = textBoomBim.text.toString()
        val target = "붐빔"
        val start = full.indexOf(target)

        if (start >= 0) {
            val spannable = SpannableString(full)
            val color = ContextCompat.getColor(this@SocialLoginActivity, R.color.main_color)
            spannable.setSpan(
                ForegroundColorSpan(color),
                start,
                start + target.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textBoomBim.text = spannable
        }
    }

    private fun permissionCheck() {
        val permissions = mutableListOf<String>()

        // ✅ 알림 권한 (Android 13 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        // ✅ 위치 권한
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        // ✅ 모든 권한을 한 번에 요청
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            lifecycleScope.launch {
                mainViewModel.setNotificationAllowed(true)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    lifecycleScope.launch {
                        mainViewModel.setNotificationAllowed(true)
                    }
                } else {
                    lifecycleScope.launch {
                        mainViewModel.setNotificationAllowed(false)
                    }
                }
            }
        }
    }
}
