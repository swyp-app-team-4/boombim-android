package com.example.swift.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.boombim.android.databinding.ActivityMainBinding
import com.boombim.android.databinding.ActivitySplashBinding
import com.example.swift.view.auth.SocialLoginActivity
import com.example.swift.view.onboarding.OnBoardingActivity
import com.example.swift.viewmodel.MainViewModel
import com.example.swift.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.checkSplash { dest ->
            Log.d("SplashActivity",dest)
            when (dest) {
                "홈프래그먼트" -> {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("dest", "home")
                    }
                    startActivity(intent)
                }
                "온보딩엑티비티" -> {
                    val intent = Intent(this, OnBoardingActivity::class.java)
                    startActivity(intent)
                }
                "소셜로그인프래그먼트" -> {
                    val intent = Intent(this, SocialLoginActivity::class.java)
                    startActivity(intent)
                }
            }
            finish()
        }
    }
}
