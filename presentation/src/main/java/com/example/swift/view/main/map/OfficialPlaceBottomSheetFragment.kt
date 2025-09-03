package com.example.swift.view.main.map

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.boombim.android.R
import com.boombim.android.databinding.FragmentPlaceBottomSheetBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.domain.model.CongestionData
import com.example.domain.model.PlaceData
import com.example.swift.util.DateTimeUtils
import com.example.swift.util.MapUtil
import com.example.swift.viewmodel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OfficialPlaceBottomSheetFragment(
    private val place: CongestionData
) : BottomSheetDialogFragment() {

    private var _binding: FragmentPlaceBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel: MapViewModel by activityViewModels()

    override fun getTheme(): Int = R.style.RoundedBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            mapViewModel.fetchOfficial(place.officialPlaceId)
        }

        setupBottomSheet()

        binding.textPlaceName.text = place.officialPlaceName

        Glide.with(binding.mapView.context)
            .load(place.imageUrl)
            .centerCrop()
            .into(binding.mapView)


        binding.iconBoombim.setImageResource(
            when (place.congestionLevelName) {
                "여유" -> R.drawable.icon_calm_small
                "보통" -> R.drawable.icon_normal_small
                "약간 붐빔" -> R.drawable.icon_slightly_busy_small
                else -> R.drawable.icon_busy_small
            }
        )

        observeOfficialPlace()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeOfficialPlace() {
        viewLifecycleOwner.lifecycleScope.launch {
            mapViewModel.officialPlace.collectLatest { placeData ->
                placeData?.let { updateDemographicsUI(it) }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun updateDemographicsUI(data: PlaceData) {
        // 성별
        updateProgress(
            data = data,
            category = "GENDER",
            subCategory = "MALE",
            progressBar = binding.progressMale,
            textView = binding.textMalePercentage
        )
        updateProgress(
            data = data,
            category = "GENDER",
            subCategory = "FEMALE",
            progressBar = binding.progressFemale,
            textView = binding.textFemalePercentage
        )

        // 거주지
        updateProgress(
            data = data,
            category = "RESIDENCY",
            subCategory = "RESIDENT",
            progressBar = binding.progressStay,
            textView = binding.textStayPercentage
        )
        updateProgress(
            data = data,
            category = "RESIDENCY",
            subCategory = "NON_RESIDENT",
            progressBar = binding.progressUnStay,
            textView = binding.textUnStayPercentage
        )

        val ageBinding = binding.agePercentageInclude

        val ageMap = mapOf(
            "0s" to ageBinding.textUnder10Percentage,
            "10s" to ageBinding.text10Percentage,
            "20s" to ageBinding.text20Percentage,
            "30s" to ageBinding.text30Percentage,
            "40s" to ageBinding.text40Percentage,
            "50s" to ageBinding.text50Percentage,
            "60s" to ageBinding.text60Percentage,
            "70s" to ageBinding.text70Percentage
        )

        binding.textUpdateTime.text = DateTimeUtils.getTimeAgo(data.observedAt)

        data.demographics.filter { it.category == "AGE_GROUP" }.forEach { age ->
            ageMap[age.subCategory]?.text = "${age.rate} %"
        }
    }

    private fun updateProgress(
        data: PlaceData,
        category: String,
        subCategory: String,
        progressBar: ProgressBar,
        textView: TextView
    ) {
        val rate = data.demographics.find {
            it.category == category && it.subCategory == subCategory
        }?.rate ?: 0.0

        progressBar.progress = rate.toInt()
        textView.text = "${rate}%"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
