package com.example.swift.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.boombim.android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutDialog (
    private val onConfirm: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

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