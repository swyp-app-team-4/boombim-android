package com.example.swift.view.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.swift.view.dialog.LoadingAlertProvider
import com.example.swift.view.main.map.helper.MarkerBitmapCache
import com.example.swift.viewmodel.FavoriteViewModel
import com.example.swift.viewmodel.MapViewModel

abstract class MapBaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null

    protected abstract val markerBitmapCache: MarkerBitmapCache

    protected val binding get() = _binding!!

    protected val mapViewModel: MapViewModel by activityViewModels()
    
    protected val favoriteViewModel: FavoriteViewModel by activityViewModels()

    protected val loadingAlertProvider by lazy { LoadingAlertProvider(this) }

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

        markerBitmapCache.clear()
    }
}
