package com.example.swift.view.main.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.BottomSheetNearByCongestionBinding
import com.boombim.android.databinding.FragmentMemberPlaceBottomSheetBinding
import com.example.domain.model.CongestionData
import com.example.domain.model.Coordinate
import com.example.domain.model.MemberCongestionItem
import com.example.swift.util.DateTimeUtils
import com.example.swift.view.main.notification.adapter.EventNotificationAdapter
import com.example.swift.view.main.map.adapter.MemberPlaceDetailAdapter
import com.example.swift.view.main.map.adapter.NearByAdapter
import com.example.swift.viewmodel.FavoriteViewModel
import com.example.swift.viewmodel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NearByCongestionBottomSheet: BottomSheetDialogFragment() {

    private var _binding: BottomSheetNearByCongestionBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.RoundedBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetNearByCongestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBottomSheet()
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false) // 외부 터치로 닫히지 않게
        dialog?.setCancelable(false)
        dialog?.window?.setDimAmount(0f)// 필요시 뒤로가기 방지
    }

    private fun initBottomSheet() {
        dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            ?.apply {
                clipToOutline = true

                val behavior = BottomSheetBehavior.from(this)
                val screenHeight = resources.displayMetrics.heightPixels
                behavior.peekHeight = (screenHeight * 0.1).toInt()

                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                behavior.isDraggable = true
                behavior.isHideable = false // 완전히 숨길 수 없도록 설정

                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams = layoutParams

                // 유저가 내려도 항상 COLLAPSED 유지
                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        // 슬라이드 중 동작 필요 없으면 비워두기
                    }
                })
            }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
