package com.example.swift.view.main.map.helper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.model.CongestionData
import com.example.domain.model.MemberPlaceData
import com.example.swift.view.main.map.MemberPlaceBottomSheetFragment
import com.example.swift.view.main.map.OfficialPlaceBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import android.view.View

class BottomSheetHelper {

    fun initBottomSheet(bottomSheet: View, peekRatio: Double = 0.1, maxRatio: Double = 0.7) {
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior.isHideable = false
        behavior.peekHeight = (bottomSheet.resources.displayMetrics.heightPixels * peekRatio).toInt()
        behavior.maxHeight = (bottomSheet.resources.displayMetrics.heightPixels * maxRatio).toInt()
    }

    fun showOfficialPlaceSheet(place: CongestionData, fragmentManager: androidx.fragment.app.FragmentManager) {
        val bottomSheet = OfficialPlaceBottomSheetFragment(place)
        bottomSheet.show(fragmentManager, bottomSheet.tag)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showMemberPlaceSheet(place: MemberPlaceData.Place, fragmentManager: androidx.fragment.app.FragmentManager) {
        val bottomSheet = MemberPlaceBottomSheetFragment(place)
        bottomSheet.show(fragmentManager, bottomSheet.tag)
    }
}

