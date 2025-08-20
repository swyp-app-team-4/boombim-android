package com.example.swift.view.main.mypage.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.boombim.android.databinding.FragmentMyPageMyVoteTabBinding
import com.example.domain.model.MyPageVoteResponse
import com.example.domain.model.PopularityDetail
import com.example.swift.view.main.mypage.adapter.MyPageVoteSectionAdapter

class MyPageMyVoteTabFragment : Fragment() {
    private var _binding: FragmentMyPageMyVoteTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyPageMyVoteTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyData = getDummyVoteData()
        val adapter = MyPageVoteSectionAdapter(dummyData)

        binding.recyclerVoteSection.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
            setHasFixedSize(true)
        }

    }

    private fun getDummyVoteData(): List<MyPageVoteResponse> {
        return listOf(
            MyPageVoteResponse(
                day = "2025-08-19",
                res = listOf(
                    PopularityDetail(
                        voteId = 3,
                        day = "2025-08-19T16:31:06.979297",
                        posName = "구로역",
                        popularStatus = "RELAXED",
                        popularCnt = "1"
                    ),
                    PopularityDetail(
                        voteId = 2,
                        day = "2025-08-19T16:29:34.428669",
                        posName = "부천역",
                        popularStatus = "CROWDED",
                        popularCnt = "1"
                    )
                )
            ),
            MyPageVoteResponse(
                day = "2025-08-18",
                res = listOf(
                    PopularityDetail(
                        voteId = 1,
                        day = "2025-08-18T16:40:11.044104",
                        posName = "부평역",
                        popularStatus = "BUSY",
                        popularCnt = "1"
                    )
                )
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}