package com.example.swift.view.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.FragmentKakaoSearchBinding
import com.example.swift.view.main.vote.VoteBaseFragment
import com.example.swift.view.main.vote.adapter.KakaoSearchListAdapter
import com.example.swift.viewmodel.KakaoSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KakaoSearchFragment : VoteBaseFragment<FragmentKakaoSearchBinding>(
    FragmentKakaoSearchBinding::inflate
) {
    private lateinit var adapter: KakaoSearchListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initKakaoSearchList()
        initSearchViewListener()
        initTextStyle()

    }

    private fun initTextStyle() {
        val searchEditText = binding.searchView.findViewById<EditText>(
            androidx.appcompat.R.id.search_src_text
        )

        searchEditText.textSize = 14f // 글자 크기
        searchEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        searchEditText.setHintTextColor(
            ContextCompat.getColor(requireContext(), R.color.gray_scale_8)
        )
        searchEditText.typeface = ResourcesCompat.getFont(requireContext(), R.font.pretend_medium)
    }

    private fun initSearchViewListener() = with(binding) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { kakaoSearchViewmodel.fetchKakaoSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { kakaoSearchViewmodel.fetchKakaoSearch(it) }
                return true
            }
        })
    }

    private fun initKakaoSearchList() = with(binding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                kakaoSearchViewmodel.kakaoSearchList.collect { list ->
                    adapter = KakaoSearchListAdapter(
                        list,
                        onItemClick = { place ->
                            val bundle = Bundle().apply {
                                putString("x", place.x)
                                putString("y", place.y)
                            }
                            findNavController().navigate(
                                R.id.mapFragment,
                                bundle
                            )
                        }
                    )
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = adapter

                }
            }
        }
    }
}

