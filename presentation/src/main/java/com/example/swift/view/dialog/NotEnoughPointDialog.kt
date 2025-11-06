package com.example.swift.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.boombim.android.R
import kotlinx.coroutines.launch

class NotEnoughPointDialog : DialogFragment()  {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.not_enough_point_dialog)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.findViewById<LinearLayout>(R.id.btn_make_congestion).setOnClickListener {
            findNavController().navigate(R.id.makeCongestionFragment)

            dismiss()
        }

        dialog.findViewById<ImageView>(R.id.icon_close).setOnClickListener {
            dismiss()
        }

        return dialog
    }
}