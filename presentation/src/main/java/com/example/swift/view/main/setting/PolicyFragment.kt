package com.example.swift.view.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentPolicyBinding
import com.boombim.android.databinding.FragmentSettingBinding
import com.example.swift.view.dialog.LogoutDialog
import com.example.swift.viewmodel.SettingViewModel

class PolicyFragment : Fragment() {
    private var _binding: FragmentPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPolicyBinding.inflate(inflater, container, false)

        setupWebView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("url")
        val title = arguments?.getString("title")

        when(title) {
            "이용약관" -> binding.webView.loadUrl(url ?: "")
            "개인정보" -> binding.webView.loadUrl(url ?: "")
            "이용안내" -> binding.webView.loadUrl(url ?: "")
            "고객센터" -> binding.webView.loadUrl(url ?: "")
            "건의사항" -> binding.webView.loadUrl(url ?: "")
            "신고하기" -> binding.webView.loadUrl(url ?: "")
            else -> ""
        }

    }

    private fun setupWebView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true   // Notion 페이지 필수
            settings.domStorageEnabled = true   // 로컬스토리지도 필요
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view?.loadUrl(request?.url.toString())
                    return true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}