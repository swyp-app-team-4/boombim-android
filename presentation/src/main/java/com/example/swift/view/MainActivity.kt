package com.example.swift.view

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.boombim.android.R
import com.boombim.android.databinding.ActivityMainBinding
import com.example.swift.viewmodel.MainViewModel
import com.example.swift.viewmodel.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.security.MessageDigest

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
                R.id.chattingFragment,
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

