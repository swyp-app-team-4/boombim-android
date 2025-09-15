package com.example.swift.view.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.swift.view.dialog.LoadingAlertProvider
import com.example.swift.viewmodel.FavoriteViewModel
import com.example.swift.viewmodel.MyPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class MyPageBaseFragment<VB : androidx.viewbinding.ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected val myPageViewModel: MyPageViewModel by activityViewModels()
    protected val favoriteViewModel: FavoriteViewModel by activityViewModels()

    protected val loadingAlertProvider by lazy {
        LoadingAlertProvider(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun collectOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    /** NavController 간단 이동 */
    protected fun navigateTo(destinationId: Int, args: Bundle? = null, navOptions: androidx.navigation.NavOptions? = null) {
        findNavController().navigate(destinationId, args, navOptions)
    }
}
