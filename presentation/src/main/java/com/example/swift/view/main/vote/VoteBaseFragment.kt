package com.example.swift.view.main.vote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.swift.view.dialog.LoadingAlertProvider
import com.example.swift.viewmodel.KakaoSearchViewModel

abstract class VoteBaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private val loadingAlertProvider: LoadingAlertProvider by lazy {
        LoadingAlertProvider(this)
    }

    protected val kakaoSearchViewmodel: KakaoSearchViewModel by activityViewModels()

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showLoading(msg: String = "") {
        loadingAlertProvider.startLoading(msg)
    }

    protected fun hideLoading() {
        loadingAlertProvider.endLoading()
    }


}
