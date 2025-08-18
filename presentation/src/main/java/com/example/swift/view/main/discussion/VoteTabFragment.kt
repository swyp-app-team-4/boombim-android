package com.example.swift.view.main.discussion

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.databinding.FragmentVoteTabBinding
import com.example.swift.view.dialog.AskingVoteFragment
import com.example.swift.view.main.discussion.adapter.VoteAdapter
import com.example.swift.viewmodel.NotificationViewModel
import com.example.swift.viewmodel.VoteViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.model.VoteItem
import com.example.swift.util.LocationUtils
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VoteTabFragment : Fragment() {
    private var _binding: FragmentVoteTabBinding? = null
    private val binding get() = _binding!!
    private val voteViewModel: VoteViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVoteTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentLocationAndFetch()

        initVote()
    }

    private fun initVote() = with((binding)){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                voteViewModel.voteList.collect{ voteList ->
                    val adapter = VoteAdapter(
                        voteList,
                        onVoteClick = { voteModel ->
                            if (voteModel.selectedIcon == -1) {
                                return@VoteAdapter
                            }

                            val message = when (voteModel.selectedIcon) {
                                0 -> "여유"
                                1 -> "보통"
                                2 -> "약간붐빔"
                                3 -> "붐빔"
                                else -> ""
                            }

                            AskingVoteFragment(
                                message = message,
                                onConfirm = {
                                    voteViewModel.postVote(
                                        voteId = voteModel.voteId,
                                        voteAnswerType = voteModel.selectedIcon.toVoteAnswerType(),
                                        onSuccess = { successMessage ->
                                            Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()
                                        },
                                        onFail = { errorMessage ->
                                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            ).show(parentFragmentManager, "AskingVoteFragment")
                        },
                    )
                    recycleVote.layoutManager = LinearLayoutManager(requireContext())
                    recycleVote.adapter = adapter
                }
            }
        }

    }

    private fun Int.toVoteAnswerType(): String {
        return when (this) {
            0 -> "RELAXED"      // 여유
            1 -> "COMMONLY"       // 보통
            2 -> "SLIGHTLY_BUSY"      // 약간붐빔
            3 -> "CROWDED" // 붐빔
            else -> throw IllegalArgumentException("Invalid vote option: $this")
        }
    }

    private fun getCurrentLocationAndFetch() {
        lifecycleScope.launch {
            val location = LocationUtils.getLastKnownLocation(requireContext(), fusedLocationClient)
                ?: LocationUtils.requestSingleUpdate(fusedLocationClient)

            location?.let {
                voteViewModel.fetchVoteList(it.latitude, it.longitude)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}