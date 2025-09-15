package com.example.swift.view.main.mypage.tab

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.boombim.android.databinding.FragmentMyPageInterestsTabBinding
import com.example.swift.view.main.mypage.MyPageBaseFragment
import com.example.swift.view.main.mypage.adapter.MyPageInterestsPlaceAdapter
import kotlinx.coroutines.launch

class MyPageInterestsTabFragment : MyPageBaseFragment<FragmentMyPageInterestsTabBinding>(
    FragmentMyPageInterestsTabBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInterestsPlace()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initInterestsPlace() = with(binding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.favoriteList.collect { list ->
                   if(list.isEmpty()){
                       recyclerInterestPlace.visibility = View.GONE
                       addPlaceLayout.visibility = View.VISIBLE
                   } else {
                       recyclerInterestPlace.visibility = View.VISIBLE
                       addPlaceLayout.visibility = View.GONE

                       val adapter = MyPageInterestsPlaceAdapter(list)

                       recyclerInterestPlace.layoutManager =
                           GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                       recyclerInterestPlace.adapter = adapter
                   }
                }
            }
        }
    }
}