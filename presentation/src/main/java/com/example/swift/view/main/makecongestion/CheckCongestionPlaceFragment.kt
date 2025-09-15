package com.example.swift.view.main.makecongestion

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.boombim.android.R
import com.boombim.android.databinding.FragmentCheckCongestionPlaceBinding
import com.example.swift.util.MapUtil
import com.example.swift.viewmodel.HomeViewModel
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import kotlinx.coroutines.launch

class CheckCongestionPlaceFragment :
    MakeCongestionBaseFragment<FragmentCheckCongestionPlaceBinding>(FragmentCheckCongestionPlaceBinding::inflate) {

    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    private var placeName: String = ""
    private var addressName: String = ""
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private var id: Int = -1
    private var serverPlaceId: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArguments()
        setupUI()
        showMapView(longitude, latitude)
        requestCheckUserPlace()
    }

    private fun initArguments() {
        arguments?.let {
            placeName = it.getString("placeName") ?: ""
            addressName = it.getString("addressName") ?: ""
            longitude = it.getString("x")?.toDoubleOrNull() ?: 0.0
            latitude = it.getString("y")?.toDoubleOrNull() ?: 0.0
            id = it.getString("id")?.toIntOrNull() ?: -1
        }
    }

    private fun setupUI() {
        binding.textPlaceName.text = placeName
        binding.textPlaceAddress.text = addressName

        binding.btnMakeVote.setOnClickListener {
            navigateToMakeCongestion()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestCheckUserPlace() {
        lifecycleScope.launch {
            homeViewModel.checkUserPlace(
                id.toString(),
                placeName,
                latitude,
                longitude,
                addressName,
                onSuccess = { placeId -> serverPlaceId = placeId },
                onFailure = { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun navigateToMakeCongestion() {
        val bundle = Bundle().apply {
            putString("placeName", placeName)
            putString("addressName", addressName)
            putString("x", longitude.toString())
            putString("y", latitude.toString())
            putInt("id", id)
            putString("serverPlaceId", serverPlaceId.toString())
        }
        findNavController().navigate(R.id.makeCongestionFragment, bundle)
    }

    private fun showMapView(longitude: Double, latitude: Double) {
        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() = Unit
            override fun onMapError(p0: Exception?) = Unit
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kMap: KakaoMap) {
                kakaoMap = kMap
                MapUtil.addMarker(
                    context = requireContext(),
                    kakaoMap = kakaoMap,
                    latitude = latitude,
                    longitude = longitude,
                    markerResId = R.drawable.icon_red_marker,
                    moveCamera = true
                )
            }
        })
    }
}
