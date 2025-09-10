package com.example.swift.view.main.vote.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.databinding.FragmentVoteTabBinding
import com.example.swift.view.dialog.AskingVoteFragment
import com.example.swift.view.main.vote.adapter.VoteAdapter
import com.example.swift.util.LocationUtils
import com.example.swift.viewmodel.VoteViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import com.example.swift.view.main.vote.BaseViewBindingFragment

@AndroidEntryPoint
class VoteTabFragment : BaseViewBindingFragment<FragmentVoteTabBinding>(FragmentVoteTabBinding::inflate) {

    private val voteViewModel: VoteViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentLocationAndFetch()
        initVote()
    }

    private fun initVote() = with(binding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                voteViewModel.voteList.collect { voteList ->

                    // 리스트가 비어있으면 add_place_layout 보이기
                    addPlaceLayout.visibility = if (voteList.isEmpty()) View.VISIBLE else View.GONE

                    val adapter = VoteAdapter(
                        onVoteClick = { voteModel ->
                            if (voteModel.selectedIcon == -1) return@VoteAdapter

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
                                    showLoading()
                                    voteViewModel.postVote(
                                        voteId = voteModel.voteId,
                                        voteAnswerType = voteModel.selectedIcon.toVoteAnswerType(),
                                        onSuccess = { successMessage ->
                                            hideLoading()
                                            Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()
                                        },
                                        onFail = { errorMessage ->
                                            hideLoading()
                                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            ).show(parentFragmentManager, "AskingVoteFragment")
                        },
                    )
                    recycleVote.layoutManager = LinearLayoutManager(requireContext())
                    recycleVote.adapter = adapter
                    adapter.submitList(voteList)
                }
            }
        }
    }

    private fun Int.toVoteAnswerType(): String {
        return when (this) {
            0 -> "RELAXED"
            1 -> "COMMONLY"
            2 -> "BUSY"
            3 -> "CROWDED"
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
}
