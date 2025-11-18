package com.example.swift.view.main.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.boombim.android.R
import com.boombim.android.databinding.FragmentHomePlaceDetailBinding
import com.boombim.android.databinding.FragmentMemberPlaceBottomSheetBinding
import com.bumptech.glide.Glide
import com.example.domain.model.PlaceData
import com.example.swift.util.DateTimeUtils
import com.example.swift.view.dialog.LoadingAlertProvider
import com.example.swift.viewmodel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomePlaceDetailFragment : Fragment() {

    private var _binding: FragmentHomePlaceDetailBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel: MapViewModel by activityViewModels()

    private var placeId: Int = -1

    private val loadingAlertProvider by lazy {
        LoadingAlertProvider(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePlaceDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeId = arguments?.getInt("placeId") ?: -1
        Log.d("HomePlaceDetailFragment", "Received placeId: $placeId")

        viewLifecycleOwner.lifecycleScope.launch {
            mapViewModel.isLoading.collectLatest { isLoading ->
                if (isLoading) {
                    loadingAlertProvider.startLoading()   // 로딩 다이얼로그 띄우기
                } else {
                    loadingAlertProvider.endLoading() // 로딩 다이얼로그 닫기
                }
            }
        }


        lifecycleScope.launch {
            mapViewModel.fetchOfficial(placeId)

           mapViewModel.officialPlace.collect{ place ->
               place ?: return@collect

               binding.textPlaceName.text = place.officialPlaceName
               binding.textPlaceAddress.text = place.legalDong
               binding.textMaxPopulationValue.text = "${place.maximumPopulation}명"
               binding.textMinPopulationValue.text = "${place.minimumPopulation}명"

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
           }
       }

        observeOfficialPlace()

        binding.iconBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
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