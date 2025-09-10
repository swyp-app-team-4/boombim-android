package com.example.swift.view.main.vote.makevote

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.boombim.android.R
import com.boombim.android.databinding.FragmentCheckMakeVoteBinding
import com.example.swift.util.LocationUtils
import com.example.swift.util.MapUtil
import com.example.swift.view.dialog.CompleteMakeVoteDialog
import com.example.swift.view.main.vote.BaseViewBindingFragment
import com.example.swift.viewmodel.VoteViewModel
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import kotlinx.coroutines.launch

class CheckMakeVoteFragment :
    BaseViewBindingFragment<FragmentCheckMakeVoteBinding>(FragmentCheckMakeVoteBinding::inflate) {

    private val voteViewModel: VoteViewModel by activityViewModels()

    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placeName = arguments?.getString("placeName") ?: ""
        val addressName = arguments?.getString("addressName") ?: ""
        val longitude = arguments?.getString("x") ?: ""
        val latitude = arguments?.getString("y") ?: ""
        val id = arguments?.getString("id")?.toIntOrNull() ?: -1

        binding.textPlaceName.text = placeName
        binding.textPlaceAddress.text = addressName

        binding.btnMakeVote.setOnClickListener {
            getUserLocationAndVote(
                postId = id,
                posLat = latitude,
                posLng = longitude,
                posName = placeName,
                address = addressName
            )
        }

        showMapView(longitude.toDouble(), latitude.toDouble())
    }

    private fun showMapView(longitude: Double, latitude: Double) {
        mapView = binding.mapView

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출
            }

            override fun onMapError(p0: Exception?) {
                // 인증 실패 및 지도 사용 중 에러 발생
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaomap: KakaoMap) {
                kakaoMap = kakaomap

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

    private fun getUserLocationAndVote(
        postId: Int,
        posLat: String,
        posLng: String,
        posName: String,
        address: String
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        lifecycleScope.launch {
            showLoading()

            val userLocation = LocationUtils.getLastKnownLocation(requireContext(), fusedLocationClient)
                ?: LocationUtils.requestSingleUpdate(fusedLocationClient)

            if (userLocation != null) {
                val userLat = userLocation.latitude.toString()
                val userLng = userLocation.longitude.toString()

                voteViewModel.makeVote(
                    postId, posLat, posLng, userLat, userLng, posName, address,
                    onSuccess = {
                        hideLoading()
                        findNavController().navigate(R.id.chattingFragment)
                        CompleteMakeVoteDialog().show(
                            parentFragmentManager,
                            "CompleteMakeVoteDialog"
                        )
                    },
                    onFail = { msg ->
                        hideLoading()
                        val errorMessage = when (msg) {
                            "403" -> "장소가 300m를 초과했습니다."
                            "409" -> "이미 해당 장소의 투표가 존재합니다."
                            else -> msg ?: "투표가 이미 존재합니다"
                        }
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    },
                )
            } else {
                hideLoading()
                Toast.makeText(requireContext(), "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
