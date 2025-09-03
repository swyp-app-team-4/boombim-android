package com.example.swift.view.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMemberPlaceBottomSheetBinding
import com.boombim.android.databinding.FragmentPlaceBottomSheetBinding
import com.example.domain.model.CongestionData
import com.example.domain.model.MemberPlaceData
import com.example.swift.viewmodel.FavoriteViewModel
import com.example.swift.viewmodel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.MapView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MemberPlaceBottomSheetFragment (
    private val place: MemberPlaceData
) : BottomSheetDialogFragment() {

    private var _binding: FragmentMemberPlaceBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    override fun getTheme(): Int = R.style.RoundedBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberPlaceBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomSheet()

        binding.textPlaceName.text = place.name

        updateFavoriteIcon(place.isFavorite)

        binding.buttonAnnounce.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.makeCongestionFragment)
        }
        
        binding.iconFavorite.setOnClickListener {
            toggleFavorite()
        }

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

    private fun toggleFavorite() {
        if (place.isFavorite) {
            favoriteViewModel.deleteFavorite(
                place.memberPlaceId,
                onSuccess = {
                    setFavoriteState(false)
                },
                onFail = {}
            )
        } else {
            // 즐겨찾기 아님 → 등록 API 호출
            favoriteViewModel.postFavorite(
                place.memberPlaceId,
                onSuccess = {
                    setFavoriteState(true)
                },
                onFail = {}
            )
        }
    }

    private fun setFavoriteState(isFavorite: Boolean) {
        place.isFavorite = isFavorite
        updateFavoriteIcon(isFavorite)
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconRes = if (isFavorite) {
            R.drawable.icon_star_yellow
        } else {
            R.drawable.icon_star
        }
        binding.iconFavorite.setImageResource(iconRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}