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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BuyTicketCompleteDialog() : DialogFragment() {

    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.buy_ticket_complete_dialog)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            dismiss()
        }

        dialog.findViewById<TextView>(R.id.text_ticket_amount).text = ""

        lifecycleScope.launch {
            myPageViewModel.eventInfo.collect { eventInfo ->
                dialog.findViewById<TextView>(R.id.text_ticket_amount).text = "${eventInfo.currentTicket}번째 응모권 구매 완료!"
            }
        }

        return dialog
    }
}