package com.example.swift.view.main.makecongestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.swift.viewmodel.HomeViewModel
import com.example.swift.viewmodel.MakeCongestionViewModel

abstract class MakeCongestionBaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected val homeViewModel: HomeViewModel by activityViewModels()
    protected val makeCongestionViewModel: MakeCongestionViewModel by activityViewModels()

    protected val lifecycleScope: LifecycleCoroutineScope
        get() = viewLifecycleOwner.lifecycleScope

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    protected fun View.show() { visibility = View.VISIBLE }
    protected fun View.hide() { visibility = View.GONE }
}