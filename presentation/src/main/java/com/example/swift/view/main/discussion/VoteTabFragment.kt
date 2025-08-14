package com.example.swift.view.main.discussion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.FragmentMyPageBinding
import com.boombim.android.databinding.FragmentVoteTabBinding
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.domain.model.VoteModel
import com.example.swift.view.main.discussion.adapter.VoteAdapter
import com.example.swift.view.main.home.adapter.InterestsPlaceAdapter
import com.example.swift.view.main.home.adapter.PlaceBoomBimAdapter
import com.example.swift.view.main.home.adapter.PlaceLessBoomBimAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoteTabFragment : Fragment() {
    private var _binding: FragmentVoteTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVoteTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVote()

    }

    private fun initVote() = with((binding)){
        val placeList = listOf(
            VoteModel(1,"asdasdsadasd" ),    // 혼잡
            VoteModel(1,"asdasdsadasd" ),    // 혼잡
            VoteModel(1,"asdasdsadasd" ),    // 혼잡
            VoteModel(1,"asdasdsadasd" ),    // 혼잡
            VoteModel(1,"asdasdsadasd" ),    // 혼잡

        )

        val adapter = VoteAdapter(
            placeList,
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

                AskingVoteFragment(message) {

                }.show(parentFragmentManager, "AskingVoteFragment")
            },
        )

        recycleVote.layoutManager = LinearLayoutManager(requireContext())
        recycleVote.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}