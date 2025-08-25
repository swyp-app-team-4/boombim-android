package com.example.swift.view.main.map

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.boombim.android.R
import com.boombim.android.databinding.FragmentPlaceBottomSheetBinding
import com.example.domain.model.CongestionData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle

class PlaceBottomSheetFragment(
    private val place: CongestionData
) : BottomSheetDialogFragment() {

    private var _binding: FragmentPlaceBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    override fun getTheme(): Int = R.style.RoundedBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomSheet()
        setupMap(place.coordinate.longitude, place.coordinate.latitude)

        binding.textPlaceName.text = place.name
    }

    private fun setupBottomSheet() {
        dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.apply {

            background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_bottom_sheet_white_20)
            clipToOutline = true

            val behavior = BottomSheetBehavior.from(this)
            val screenHeight = resources.displayMetrics.heightPixels
            behavior.peekHeight = (screenHeight * 0.5).toInt()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.isDraggable = true
            behavior.isHideable = true

            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams = layoutParams
        }
    }

    private fun setupMap(longitude: Double, latitude: Double) {
        mapView = binding.mapView

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(p0: Exception?) {}
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaomap: KakaoMap) {
                kakaoMap = kakaomap
                addMarker(latitude, longitude)
            }
        })
    }

    private fun addMarker(x: Double, y: Double) {
        kakaoMap?.labelManager?.layer?.let { layer ->
            val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_red_marker)
            val position = LatLng.from(x, y)
            val iconOptions = LabelOptions.from(position)
                .setStyles(LabelStyle.from(markerBitmap))

            layer.addLabel(iconOptions)
            kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(x, y)))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
