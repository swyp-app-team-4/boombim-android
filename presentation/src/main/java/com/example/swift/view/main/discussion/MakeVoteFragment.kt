package com.example.swift.view.main.discussion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.FragmentChattingBinding
import com.boombim.android.databinding.FragmentMakeVoteBinding
import com.example.swift.view.main.discussion.adapter.KakaoSearchListAdapter
import com.example.swift.viewmodel.AuthViewModel
import com.example.swift.viewmodel.KakaoSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MakeVoteFragment : Fragment() {
    private var _binding: FragmentMakeVoteBinding? = null
    private val binding get() = _binding!!
    private val kakaoSearchViewmodel: KakaoSearchViewModel by activityViewModels()
    private lateinit var adapter: KakaoSearchListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMakeVoteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initKakaoSearchList()

        initSearchViewListener()

        binding.iconBack.setOnClickListener {
            findNavController().navigate(
                R.id.chattingFragment, null,
                navOptions {
                    popUpTo(R.id.makeVoteFragment) {
                        inclusive = true
                    }
                }
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
                    adapter = KakaoSearchListAdapter(list)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = adapter

                    updateLogoVisibility(list.size)
                }
            }
        }
    }

    private fun updateLogoVisibility(listSize: Int) {
        binding.image3dLogoCurious.visibility = if (listSize > 0) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}