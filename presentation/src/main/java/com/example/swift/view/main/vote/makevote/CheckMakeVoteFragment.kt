package com.example.swift.view.main.vote.makevote

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.boombim.android.R
import com.boombim.android.databinding.FragmentCheckMakeVoteBinding
import com.boombim.android.databinding.FragmentMyPageBinding
import com.example.swift.util.LocationUtils
import com.example.swift.util.MapUtil
import com.example.swift.view.dialog.CompleteMakeVoteDialog
import com.example.swift.viewmodel.VoteViewModel
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckMakeVoteFragment : Fragment() {
    private var _binding: FragmentCheckMakeVoteBinding? = null
    private val binding get() = _binding!!
    private val voteViewModel: VoteViewModel by activityViewModels()

    private lateinit var mapView : MapView
    private var kakaoMap : KakaoMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCheckMakeVoteBinding.inflate(inflater, container, false)

        return binding.root
    }

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
                posName = placeName
            )
        }


        showMapView(longitude!!.toDouble(),latitude!!.toDouble())
    }

    private fun showMapView(longitude: Double, latitude: Double){

        mapView = binding.mapView

        mapView.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출

            }

            override fun onMapError(p0: Exception?) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출

            }
        }, object : KakaoMapReadyCallback(){
            override fun onMapReady(kakaomap: KakaoMap) {
                kakaoMap = kakaomap

                MapUtil.addMarker(
                    context = requireContext(),
                    kakaoMap = kakaoMap,
                    latitude = latitude,
                    longitude = longitude,
                    markerResId = R.drawable.image_green_pin,
                    moveCamera = true
                )

            }
        })
    }

    private fun getUserLocationAndVote(
        postId: Int,
        posLat: String,
        posLng: String,
        posName: String
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        lifecycleScope.launch {
            val userLocation = LocationUtils.getLastKnownLocation(requireContext(), fusedLocationClient)
                ?: LocationUtils.requestSingleUpdate(fusedLocationClient)

            if (userLocation != null) {
                val userLat = userLocation.latitude.toString()
                val userLng = userLocation.longitude.toString()
//
//                val userLat = "37.50437663505579"
//                val userLng = "127.04897066287083"


                voteViewModel.makeVote(
                    postId, posLat, posLng, userLat, userLng, posName,
                    onSuccess = { msg ->
                        findNavController().navigate(R.id.chattingFragment)
                        CompleteMakeVoteDialog().show(parentFragmentManager, "CompleteMakeVoteDialog")
                    },
                    onFail = { msg ->
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}