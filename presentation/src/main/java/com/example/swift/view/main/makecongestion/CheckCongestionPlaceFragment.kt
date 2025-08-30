package com.example.swift.view.main.makecongestion

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentCheckCongestionPlaceBinding
import com.boombim.android.databinding.FragmentCheckMakeVoteBinding
import com.boombim.android.databinding.FragmentMakeCongestionBinding
import com.example.swift.util.LocationUtils
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
import kotlinx.coroutines.launch

class CheckCongestionPlaceFragment : Fragment() {
    private var _binding: FragmentCheckCongestionPlaceBinding? = null
    private val binding get() = _binding!!
    private val voteViewModel: VoteViewModel by activityViewModels()

    private var placeName: String = ""
    private var addressName: String = ""
    private var longitude: String = ""
    private var latitude: String = ""
    private var id: Int = -1

    private lateinit var mapView : MapView
    private var kakaoMap : KakaoMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCheckCongestionPlaceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeName = arguments?.getString("placeName") ?: ""
        addressName = arguments?.getString("addressName") ?: ""
        longitude = arguments?.getString("x") ?: ""
        latitude = arguments?.getString("y") ?: ""
        id = arguments?.getString("id")?.toIntOrNull() ?: -1

        binding.textPlaceName.text = placeName
        binding.textPlaceAddress.text = addressName

        showMapView(longitude!!.toDouble(),latitude!!.toDouble())

        binding.btnMakeVote.setOnClickListener {
            val bundle = Bundle().apply {
                putString("placeName", placeName)
                putString("addressName", addressName)
                putString("x", longitude)
                putString("y", latitude)
                putInt("id", id)
            }
            findNavController().navigate(R.id.makeCongestionFragment, bundle)
        }
    }

    private fun showMapView(x: Double, y: Double){

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

                addMarker(y, x)

            }
        })
    }

    private fun addMarker(x: Double, y: Double){
        kakaoMap?.let { map ->
            val layer = map.labelManager?.layer ?: return

            val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_red_marker)
            val position = LatLng.from(x, y)
            val iconStyle = LabelStyle.from(markerBitmap)

            val iconOptions = LabelOptions.from(position)
                .setStyles(iconStyle)

            layer.addLabel(iconOptions)

            map.moveCamera(
                CameraUpdateFactory.newCenterPosition(
                    LatLng.from(x,y)
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}