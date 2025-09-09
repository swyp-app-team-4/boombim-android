package com.example.swift.view.main.map

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMemberPlaceBottomSheetBinding
import com.boombim.android.databinding.FragmentPlaceBottomSheetBinding
import com.example.domain.model.CongestionData
import com.example.domain.model.MemberCongestionItem
import com.example.domain.model.MemberPlaceData
import com.example.swift.util.DateTimeUtils
import com.example.swift.view.main.map.adapter.MemberPlaceDetailAdapter
import com.example.swift.viewmodel.FavoriteViewModel
import com.example.swift.viewmodel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.MapView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class MemberPlaceBottomSheetFragment(
    private val place: MemberPlaceData
) : BottomSheetDialogFragment() {

    private var _binding: FragmentMemberPlaceBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()

    private lateinit var detailAdapter: MemberPlaceDetailAdapter

    override fun getTheme(): Int = R.style.RoundedBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberPlaceBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBottomSheet()
        initUi()
        initRecyclerView()

        // 데이터 불러오기
        mapViewModel.fetchMemberPlaceList(place.memberPlaceId)
    }

    override fun onResume() {
        super.onResume()
        mapViewModel.fetchMemberPlaceList(place.memberPlaceId)
    }

    private fun initUi() {
        binding.textPlaceName.text = place.name
        binding.textTime.text = DateTimeUtils.getTimeAgo(place.createdAt)
        updateFavoriteUi(place.isFavorite)

        binding.buttonAnnounce.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.chattingFragment)
        }

        binding.iconFavorite.setOnClickListener {
            handleFavoriteClick()
        }

        binding.iconBoombim.setImageResource(
            when (place.congestionLevelName) {
                "여유" -> R.drawable.icon_calm_small
                "보통" -> R.drawable.icon_normal_small
                "약간 붐빔" -> R.drawable.icon_slightly_busy_small
                else -> R.drawable.icon_busy_small
            }
        )
    }

    private fun initBottomSheet() {
        dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            ?.apply {
                background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_bottom_sheet_white_20
                )
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

    private fun initRecyclerView() {
        detailAdapter = MemberPlaceDetailAdapter(
            emptyList(),
            onReportClick = {
                val bundle = Bundle().apply {
                    dismiss()
                    putString("url", "https://naver.me/F8uupoGw")
                    putString("title", "신고하기")
                }

                findNavController().navigate(R.id.policyFragment, bundle)
            }
        )
        binding.recycler.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mapViewModel.memberPlaceDetailList.collect { list ->
                detailAdapter.updateItems(list)
                setupFilterButtons(list)
            }
        }
    }

    private fun setupFilterButtons(originalList: List<MemberCongestionItem>) {
        val buttons = listOf(
            binding.containerCongestion.getChildAt(0) as TextView, // 최신순
            binding.containerCongestion.getChildAt(1) as TextView, // 붐빔
            binding.containerCongestion.getChildAt(2) as TextView, // 약간붐빔
            binding.containerCongestion.getChildAt(3) as TextView, // 보통
            binding.containerCongestion.getChildAt(4) as TextView  // 여유
        )

        fun selectButton(selected: TextView) {
            buttons.forEach { it.isSelected = false }
            selected.isSelected = true
        }

        buttons[0].setOnClickListener {
            selectButton(it as TextView)
            val sorted = originalList.sortedByDescending { item -> item.createdAt }
            detailAdapter.updateItems(sorted)
        }

        buttons[1].setOnClickListener {
            selectButton(it as TextView)
            val filtered = originalList.filter { item -> item.congestionLevelName == "붐빔" }
            detailAdapter.updateItems(filtered)
        }

        buttons[2].setOnClickListener {
            selectButton(it as TextView)
            val filtered = originalList.filter { item -> item.congestionLevelName == "약간붐빔" }
            detailAdapter.updateItems(filtered)
        }

        buttons[3].setOnClickListener {
            selectButton(it as TextView)
            val filtered = originalList.filter { item -> item.congestionLevelName == "보통" }
            detailAdapter.updateItems(filtered)
        }

        buttons[4].setOnClickListener {
            selectButton(it as TextView)
            val filtered = originalList.filter { item -> item.congestionLevelName == "여유" }
            detailAdapter.updateItems(filtered)
        }

        detailAdapter.updateItems(originalList.sortedByDescending { it.createdAt })
    }


    private fun handleFavoriteClick() {
        if (place.isFavorite) {
            favoriteViewModel.deleteFavorite(
                place.memberPlaceId,
                place.placeType,
                onSuccess = { applyFavoriteState(false) },
                onFail = {}
            )
        } else {
            favoriteViewModel.postFavorite(
                place.memberPlaceId,
                place.placeType,
                onSuccess = { applyFavoriteState(true) },
                onFail = {}
            )
        }
    }

    private fun applyFavoriteState(isFavorite: Boolean) {
        place.isFavorite = isFavorite
        updateFavoriteUi(isFavorite)
    }

    private fun updateFavoriteUi(isFavorite: Boolean) {
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
