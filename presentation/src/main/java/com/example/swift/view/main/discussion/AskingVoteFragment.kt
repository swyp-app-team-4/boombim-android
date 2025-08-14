package com.example.swift.view.main.discussion

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.boombim.android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskingVoteFragment(
    private val message: String,
    private val onConfirm: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_vote_asking)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        when(message) {
            "여유" -> dialog.findViewById<TextView>(R.id.text_vote_option).text = "로 투표할까요?"
            "보통" -> dialog.findViewById<TextView>(R.id.text_vote_option).text = "으로 투표할까요?"
            "약간붐빔" -> dialog.findViewById<TextView>(R.id.text_vote_option).text = "으로 투표할까요?"
            "붐빔" -> dialog.findViewById<TextView>(R.id.text_vote_option).text = "으로 투표할까요?"
            else -> dialog.findViewById<TextView>(R.id.text_vote_option).text = "이모티콘을 골라주세요"
        }

        dialog.findViewById<TextView>(R.id.text_vote_asking).text = message

        dialog.findViewById<Button>(R.id.btn_no).setOnClickListener {
            dismiss()
        }
        dialog.findViewById<Button>(R.id.btn_yes).setOnClickListener {
            onConfirm()
            dismiss()
        }

        return dialog
    }
}