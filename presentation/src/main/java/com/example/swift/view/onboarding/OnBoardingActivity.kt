package com.example.swift.view.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.boombim.android.R
import com.boombim.android.databinding.ActivityOnBoardingBinding
import com.example.domain.model.OnboardingPage
import com.example.swift.view.MainActivity
import com.example.swift.view.auth.SocialLoginActivity
import com.example.swift.viewmodel.MainViewModel
import com.example.swift.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var pages: List<OnboardingPage>
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPages()

        initViewPager()

        binding.btnSkip.setOnClickListener {
            onBoardingViewModel.updateNotFirstLaunch()
            val intent = Intent(this, SocialLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnStart.setOnClickListener {
            val currentItem = binding.container.currentItem

            if (currentItem < pages.lastIndex) {
                binding.btnStart.text = "다음"
                binding.container.currentItem = currentItem + 1
            } else {
                binding.btnStart.text = "시작하기"
                onBoardingViewModel.updateNotFirstLaunch()
                val intent = Intent(this, SocialLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun initPages() {
        pages = listOf(
            OnboardingPage("실시간으로 한산한 동네를 찾아보세요", "", R.drawable.image_onboarding_1),
            OnboardingPage("지금 내 주변은 어떨까 ?", "지도를 통해 확인해보세요 !", R.drawable.image_onboarding_2),
            OnboardingPage("지금 내 주변은 어떨까 ?", "사람들이 실시간으로 공유하는 정보를 확인할 수 있어요", R.drawable.image_onboarding_3),
            OnboardingPage("지금 내 주변은 어떨까 ?", "서울시에서 공유하는 정보도 확인할 수 있어요", R.drawable.image_onboarding_4),
            OnboardingPage("내가 알고 싶은 장소가 없다면 ?", "소통방에 질문을 올려\n장소의 붐빔을 알 수 있어요", R.drawable.image_onboarding_5),
            OnboardingPage("내 주변의 혼잡도를 알려요", "내 주변 장소를 궁금해하는 사람에게 투표를 통해 혼잡도를 알려요", R.drawable.image_onboarding_6),
        )
    }

    private fun initViewPager() {
        binding.container.adapter = OnboardingAdapter(this, pages)

        binding.dotsIndicator.attachTo(binding.container)

        binding.container.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonStyleIfLastPage(position)
            }
        })
    }

    private fun updateButtonStyleIfLastPage(position: Int) {
        if (position == pages.lastIndex) {
            binding.btnStart.apply {
                setBackgroundResource(R.drawable.bg_radius_8_background_main_color)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        } else {
            binding.btnStart.apply {
                setBackgroundResource(R.drawable.bg_rounded_8_background_gray4)
                setTextColor(ContextCompat.getColor(context, R.color.gray_scale_7))
            }
        }
    }
}
