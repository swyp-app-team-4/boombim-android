package com.example.swift.view.main.map.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.boombim.android.R
import com.boombim.android.databinding.BottomSheetNearByCongestionBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

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
                behavior.peekHeight = (screenHeight * 0.2).toInt()

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
