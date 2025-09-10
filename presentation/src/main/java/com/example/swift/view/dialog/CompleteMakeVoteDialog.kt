package com.example.swift.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.boombim.android.R
import com.example.swift.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch

class CompleteMakeVoteDialog: DialogFragment() {

    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_complete_make_vote)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.findViewById<Button>(R.id.btn_complete).setOnClickListener {
            dismiss()
        }

        lifecycleScope.launch {
            myPageViewModel.profile.collect{
                dialog.findViewById<TextView>(R.id.text_nick_name).text = it.name
            }
        }





        return dialog
    }
}