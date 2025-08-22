package com.example.swift.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.swift.ui.theme.MainColor
import com.example.swift.ui.theme.pretendard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingAlert : DialogFragment() {

    companion object {
        private const val LOADING_MSG_KEY = "loading_msg_key"
        fun getInstance(): LoadingAlert {
            return LoadingAlert()
        }

        fun getInstance(msg: String): LoadingAlert {
            return LoadingAlert().apply {
                arguments = bundleOf(
                    LOADING_MSG_KEY to msg
                )
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.requestWindowFeature(STYLE_NO_TITLE)
            setCancelable(false)
            it.setCanceledOnTouchOutside(false)
        }
        val msg = arguments?.getString(LOADING_MSG_KEY)

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                LoadingView(
                    modifier = Modifier,
                    msg = msg
                )
            }
        }
    }
}
@Composable
fun LoadingView(
    modifier: Modifier,
    msg: String?
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = MainColor
        )
        if (!msg.isNullOrEmpty()) {
            Text(
                text = msg,
                fontFamily = pretendard
            )
        }
    }
}

private const val debounceThreshold = 300L                     // 300ms 이하의 짧은 로딩은 무시
private const val minDisplayTime = 500L                        // 로딩이 표시되면 최소 500ms는 유지

class LoadingAlertProvider(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val coroutineScope: CoroutineScope, // 외부에서 lifecycleScope 등을 주입
) {

    // Fragment용 생성자: viewLifecycleOwner.lifecycleScope 사용
    constructor(fragment: Fragment) : this(
        fragment.requireContext(),
        fragment.parentFragmentManager,
        fragment.viewLifecycleOwner.lifecycleScope
    )

    // Activity용 생성자: lifecycleScope 사용
    constructor(activity: AppCompatActivity) : this(
        activity,
        activity.supportFragmentManager,
        activity.lifecycleScope
    )

    private var loadingAlert: LoadingAlert? = null           // 현재 표시 중인 로딩 다이얼로그
    private var loadingStartTime: Long = 0                   // 실제 로딩이 표시된 시점 (ms)
    private var isShowing = false                            // 로딩 다이얼로그 표시 여부
    private var showJob: Job? = null                         // 디바운스 딜레이 작업

    /**
     * 로딩을 시작합니다 (기본 메시지 없음).
     */
    fun startLoading(msg: String = "") {
        // 이전 로딩이 있었다면 제거
        showJob?.cancel()
        loadingAlert?.dismiss()
        loadingAlert = null
        isShowing = false
        loadingStartTime = System.currentTimeMillis()

        // debounceThreshold만큼 기다린 후 로딩 표시
        showJob = coroutineScope.launch {
            delay(debounceThreshold)

            // debounce 시간 경과 후 실제 로딩 다이얼로그 표시
            loadingAlert = LoadingAlert.getInstance(msg)
            loadingAlert?.show(fragmentManager, "loading")

            loadingStartTime = System.currentTimeMillis()
            isShowing = true
        }
    }

    /**
     * 로딩을 종료합니다. (최소 표시 시간 고려)
     */
    fun endLoading(withAction: () -> Unit = {}) {
        coroutineScope.launch {
            // 대기 중이던 로딩 예약이 있다면 취소
            showJob?.cancel()

            if (!isShowing) {
                // 로딩이 아예 표시되지 않았던 경우 (300ms 이내 종료)
                withAction()
                return@launch
            }

            // 표시된 경우에는 최소 유지 시간(minDisplayTime)을 만족시킴
            val elapsed = System.currentTimeMillis() - loadingStartTime
            val remaining = minDisplayTime - elapsed

            if (remaining > 0) delay(remaining)

            // 로딩 다이얼로그 종료
            loadingAlert?.dismiss()
            loadingAlert = null
            isShowing = false

            withAction()
        }
    }

    /**
     * 로딩 종료 후 Toast 메시지를 출력합니다.
     */
    fun endLoadingWithMessage(msg: String) {
        endLoading {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
