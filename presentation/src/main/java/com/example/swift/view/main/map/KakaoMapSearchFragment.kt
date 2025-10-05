package com.example.swift.view.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.boombim.android.databinding.FragmentKakaoMapSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KakaoMapSearchFragment : Fragment() {

    private var _binding: FragmentKakaoMapSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKakaoMapSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 여기에 UI 초기화 코드 작성
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
