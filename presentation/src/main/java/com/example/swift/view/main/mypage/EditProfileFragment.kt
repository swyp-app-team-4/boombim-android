package com.example.swift.view.main.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import com.boombim.android.R
import com.boombim.android.databinding.FragmentEditProfileBinding
import com.boombim.android.databinding.FragmentMyPageInterestsTabBinding
import com.bumptech.glide.Glide
import com.example.domain.model.ImageAddType
import com.example.domain.model.InterestsPlaceModel
import com.example.swift.view.dialog.ImagePickerDialogFragment
import com.example.swift.view.main.home.adapter.InterestsPlaceAdapter
import com.example.swift.viewmodel.MyPageViewModel
import com.example.swift.viewmodel.VoteViewModel
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {
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

        binding.iconBack.setOnClickListener {
            findNavController().navigate(
                R.id.myPageFragment, null,
                navOptions {
                    popUpTo(R.id.editProfileFragment) {
                        inclusive = true
                    }
                }
            )
        }

        lifecycleScope.launch {
            myPageViewModel.profile.collect{ profile->
                Glide.with(requireContext())
                    .load(profile.profile) // JSON에서 내려온 이미지 URL
                    .placeholder(R.drawable.icon_edit_profile) // 로딩 중 보여줄 기본 이미지
                    .error(R.drawable.icon_edit_profile) // 실패 시 보여줄 기본 이미지
                    .into(binding.imageProfile)
            }
        }

        binding.imageProfile.setOnClickListener {
            val dialog = ImagePickerDialogFragment.getInstance(
                handler = object : ImagePickerDialogFragment.ImageTypeHandler {
                    override fun receiveImageType(imageType: ImageAddType) {
                        when (imageType) {
                            is ImageAddType.Default -> {
//                                imageProfile.setImageResource(R.drawable.image_profile_default_cover)
//                                val defaultImageUri = Uri.parse("android.resource://${requireContext().packageName}/${R.drawable.image_profile_default_cover}")
//                                authViewModel.setProfileImage(defaultImageUri.toString())
                            }

                            is ImageAddType.Content -> {
//                                imageProfile.setImageURI(imageType.uri)
//                                imageProfile.scaleType = ImageView.ScaleType.CENTER_CROP
//                                authViewModel.setProfileImage(imageType.uri.toString())
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}