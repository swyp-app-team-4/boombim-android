package com.example.swift.view


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.boombim.android.R
import com.boombim.android.databinding.ActivityMainBinding
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        // 스플래시에서 넘어온 목적지 처리
        when (intent.getStringExtra("dest")) {
            "home" -> {
                navController.navigate(R.id.homeFragment)
            }
            "edit" -> {
                navController.navigate(R.id.socialLoginProfileSettingFragment)
            }
        }

        binding.btnMakeVote.setOnClickListener {
           navController.navigate((R.id.makeCongestionFragment))
        }

        initNavigation()

    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        binding.navBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.mapFragment,
                R.id.myPageFragment -> {
                    binding.navBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.navBar.visibility = View.GONE
                }
            }

            binding.btnMakeVote.visibility =
                if (destination.id == R.id.homeFragment) View.VISIBLE else View.GONE
        }
    }
}

