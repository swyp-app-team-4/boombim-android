package com.example.swift.view.auth

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.boombim.android.R
import com.boombim.android.databinding.FragmentSocialLoginBinding
import com.example.swift.viewmodel.AuthViewModel
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.AndroidEntryPoint
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions

@AndroidEntryPoint
class SocialLoginFragment : Fragment() {
    private var _binding: FragmentSocialLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewmodel: AuthViewModel by activityViewModels()

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                Log.d("SocialLoginFragment", NaverIdLoginSDK.getAccessToken().toString())
                Log.d("SocialLoginFragment", NaverIdLoginSDK.getRefreshToken().toString())
                handleNaverToken(
                    NaverIdLoginSDK.getAccessToken().toString(),
                    NaverIdLoginSDK.getRefreshToken().toString()
                )
            }

            RESULT_CANCELED -> {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("NaverLogin", "Error $errorCode: $errorDescription")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSocialLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnKakaoLogin.setOnClickListener {
            kakaoLogin(requireContext())
        }

        binding.btnNaverLogin.setOnClickListener {
            NaverIdLoginSDK.authenticate(requireContext(), launcher)
        }

        setTextColor()


    }

    private fun kakaoLogin(context: Context) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                UserApiClient.instance.loginWithKakaoAccount(context) { accountToken, accountError ->
                    if (accountToken != null) {
                        handleKakaoToken(accountToken.accessToken, accountToken.refreshToken)
                    } else {
                        Log.e("SocialLoginFragment", "카카오계정 로그인 실패", accountError)
                    }
                }
            } else if (token != null) {
                handleKakaoToken(token.accessToken, token.refreshToken)
            }
        }
    }

    private fun handleKakaoToken(accessToken: String, refreshToken: String) {
       authViewmodel.socialLogin(
            accessToken = accessToken,
            refreshToken = refreshToken,
            provider = "kakao",
            onSuccess = { nameFlag ->
              if(nameFlag){
                  val navOptions = NavOptions.Builder()
                      .setPopUpTo(R.id.socialLoginFragment, true)
                      .build()
                  findNavController().navigate(R.id.homeFragment, null, navOptions)
                  Toast.makeText(requireContext(), "카카오 로그인 성공", Toast.LENGTH_SHORT).show()
              }else{
                  Toast.makeText(requireContext(), "카카오 로그인 성공", Toast.LENGTH_SHORT).show()
                  findNavController().navigate(R.id.editProfileFragment)
              }
            },
            onFail = { msg ->
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun handleNaverToken(accessToken: String, refreshToken: String) {
        authViewmodel.socialLogin(
            accessToken = accessToken,
            refreshToken = refreshToken,
            provider = "naver",
            onSuccess = { nameFlag ->
                if(nameFlag){
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.socialLoginFragment, true)
                        .build()
                    findNavController().navigate(R.id.homeFragment, null, navOptions)
                    Toast.makeText(requireContext(), "네이버 로그인 성공", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "네이버 로그인 성공", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.editProfileFragment)
                }
            },
            onFail = { msg ->
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setTextColor() = with(binding){
        val full = textBoomBim.text.toString()
        val target = "붐빔"
        val start = full.indexOf(target)

        if (start >= 0) {
            val spannable = SpannableString(full)
            val color = ContextCompat.getColor(requireContext(), R.color.main_color) // 메인 컬러 리소스
            spannable.setSpan(
                ForegroundColorSpan(color),
                start,
                start + target.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textBoomBim.text = spannable
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}