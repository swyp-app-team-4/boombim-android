package com.example.swift.view.auth

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boombim.android.R
import com.boombim.android.databinding.FragmentEditProfileBinding
import com.bumptech.glide.Glide
import com.example.domain.model.ImageAddType
import com.example.swift.view.dialog.ImagePickerDialogFragment
import com.example.swift.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch

class SocialLoginProfileSettingFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackButton()

        observeProfile()

        initNicknameWatcher()

        initProfileImageClick()

        binding.btnPatch.setOnClickListener{
            myPageViewModel.patchNickName(
                binding.editNickname.text.toString(),
                onSuccess = {
                    Toast.makeText(requireContext(), "변경이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        R.id.homeFragment, null,
                        navOptions {
                            popUpTo(R.id.socialLoginProfileSettingFragment) {
                                inclusive = true
                            }
                        }
                    )
                },
                onFail = {
                    Toast.makeText(requireContext(), "변경 실패", Toast.LENGTH_SHORT).show()
                }
            )

            val tag = binding.imageProfile.tag

            if (tag is String) {
                myPageViewModel.patchProfileImage(
                    imagePath = tag
                )
            }
        }
    }

    private fun initBackButton() {
        binding.iconBack.setOnClickListener {
            findNavController().navigate(
                R.id.myPageFragment, null,
                navOptions { popUpTo(R.id.editProfileFragment) { inclusive = true } }
            )
        }
    }

    private fun observeProfile() {
        lifecycleScope.launch {
            myPageViewModel.profile.collect { profile ->
                Glide.with(requireContext())
                    .load(profile.profile)
                    .placeholder(R.drawable.icon_edit_profile)
                    .error(R.drawable.icon_edit_profile)
                    .into(binding.imageProfile)
            }
        }
    }

    private fun initNicknameWatcher() {
        binding.editNickname.addTextChangedListener { text ->
            val nickname = text?.toString()?.trim() ?: ""
            validateNickname(nickname)
        }
    }

    private fun validateNickname(nickname: String) {
        val regex = "^[a-zA-Z0-9가-힣]{1,20}$".toRegex()

        when {
            nickname.isBlank() -> {
                binding.textNicknameRule.text = "한글, 영문, 숫자 (공백포함 20자)"
                binding.textNicknameRule.setTextColor(requireContext().getColor(R.color.gray_scale_7))
                updateButtonState(false)
            }
            !regex.matches(nickname) -> {
                binding.textNicknameRule.text = "공백 없이 최대  20자까지 입력 가능합니다."
                binding.textNicknameRule.setTextColor(requireContext().getColor(R.color.red ))
                updateButtonState(false)
            }
            else -> {
                binding.textNicknameRule.text = "확인되었습니다 ."
                binding.textNicknameRule.setTextColor(requireContext().getColor(R.color.green))
                updateButtonState(true)
            }
        }
    }

    private fun updateButtonState(enabled: Boolean) {
        binding.btnPatch.apply {
            isEnabled = enabled
            if (enabled) {
                setBackgroundResource(R.drawable.bg_rounded_main_color)
                setTextColor(requireContext().getColor(R.color.white))
            } else {
                setBackgroundResource(R.drawable.bg_rounded_8_background_gray4)
                setTextColor(requireContext().getColor(R.color.gray_scale_7))
            }
        }
    }

    private fun initProfileImageClick() {
        binding.imageProfile.setOnClickListener {
            val dialog = ImagePickerDialogFragment.getInstance(
                handler = object : ImagePickerDialogFragment.ImageTypeHandler {
                    override fun receiveImageType(imageType: ImageAddType) {
                        when (imageType) {
                            is ImageAddType.Default -> {
                                binding.imageProfile.setImageResource(R.drawable.icon_profile)
                                val defaultImageUri = Uri.parse("android.resource://${requireContext().packageName}/${R.drawable.icon_profile}")
                            }
                            is ImageAddType.Content -> {
                                binding.imageProfile.setImageURI(imageType.uri)
                                binding.imageProfile.scaleType = ImageView.ScaleType.CENTER_CROP

                                val imagePath = getRealPathFromUri(imageType.uri)
                                binding.imageProfile.tag = imagePath
                            }
                            else -> {}
                        }
                    }
                },
                maxImage = 1
            )
            dialog.show(parentFragmentManager, ImagePickerDialogFragment.TAG)
        }
    }

    private fun getRealPathFromUri(uri: Uri): String? {
        val projection = arrayOf(android.provider.MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return null
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}