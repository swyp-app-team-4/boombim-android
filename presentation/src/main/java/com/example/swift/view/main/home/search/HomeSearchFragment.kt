package com.example.swift.view.main.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMyPageBinding
import com.boombim.android.databinding.FragmentSearchHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeSearchFragment: Fragment() {
    private var _binding: FragmentSearchHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextStyle()

        binding.iconBack.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment, null,
                navOptions {
                    popUpTo(R.id.notificationFragment) {
                        inclusive = true
                    }
                }
            )
        }

    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
    }

    private fun initTextStyle(){
        val searchEditText = binding.searchView.findViewById<EditText>(
            androidx.appcompat.R.id.search_src_text
        )

        searchEditText.textSize = 14f // 글자 크기
        searchEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        searchEditText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.gray_scale_8))
        searchEditText.typeface = ResourcesCompat.getFont(requireContext(), R.font.pretend_medium)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}