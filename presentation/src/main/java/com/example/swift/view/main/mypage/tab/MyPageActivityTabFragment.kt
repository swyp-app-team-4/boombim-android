package com.example.swift.view.main.mypage.tab

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.databinding.FragmentMyPageActivityTabBinding
import com.example.swift.util.groupActivitiesByDate
import com.example.swift.view.main.mypage.MyPageBaseFragment
import com.example.swift.view.main.mypage.adapter.MyPageActivityAdapter
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class MyPageActivityTabFragment : MyPageBaseFragment<FragmentMyPageActivityTabBinding>(
    FragmentMyPageActivityTabBinding::inflate) {

    private val adapter = MyPageActivityAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycle.layoutManager = LinearLayoutManager(requireContext())
        binding.recycle.adapter = adapter

        // ViewModel에서 activity 상태 수집
        viewLifecycleOwner.lifecycleScope.launch {
            myPageViewModel.activity.collect { activityList ->
                adapter.submitList(groupActivitiesByDate(activityList))
            }
        }
    }
}
