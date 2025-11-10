package com.example.swift.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.boombim.android.R

class MakeCongestionSuccessDialog(private val placeName: String): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_make_congestion_success)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            dismiss()
        }

        dialog.findViewById<TextView>(R.id.text_place_name).text = placeName

        return dialog
    }
}