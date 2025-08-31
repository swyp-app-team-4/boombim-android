package com.example.swift.view.main.makecongestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.FragmentCongestionSearchBinding
import com.boombim.android.databinding.FragmentMakeVoteBinding
import com.example.swift.view.main.vote.adapter.KakaoSearchListAdapter
import com.example.swift.viewmodel.KakaoSearchViewModel
import kotlinx.coroutines.launch

class CongestionSearchFragment : Fragment() {
    private var _binding: FragmentCongestionSearchBinding? = null
    private val binding get() = _binding!!
    private val kakaoSearchViewmodel: KakaoSearchViewModel by activityViewModels()
    private lateinit var adapter: KakaoSearchListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCongestionSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initKakaoSearchList()

        initSearchViewListener()


        binding.iconBack.setOnClickListener {
            findNavController().navigate(
                R.id.makeCongestionFragment, null,
            )
        }

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
            repeatOnLifecycle(Lifecycle.State.STARTED){
                kakaoSearchViewmodel.kakaoSearchList.collect { list ->
                    adapter = KakaoSearchListAdapter(
                        list,
                        onItemClick = { place ->
                            val bundle = Bundle().apply {
                                putString("placeName", place.place_name)
                                putString("addressName", place.address_name)
                                putString("x", place.x)
                                putString("y", place.y)
                                putString("id",place.id)
                            }
                            findNavController().navigate(R.id.checkCongestionPlaceFragment,bundle)
                        }
                    )
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = adapter

                }
            }
        }
    }

}