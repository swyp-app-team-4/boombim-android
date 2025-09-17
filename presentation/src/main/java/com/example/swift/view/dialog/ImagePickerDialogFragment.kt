package com.example.swift.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IntRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.boombim.android.R
import com.example.domain.model.ImageAddType
import com.example.swift.ui.theme.GrayScale8
import com.example.swift.ui.theme.pretendard
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun ImagePickerDialog(
    onDismiss: () -> Unit,
    updateImageType: (ImageAddType) -> Unit,
    maxImage: Int = 1
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .clickable(
                    onClick = onDismiss,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            ImagePickerDialogContent(
                modifier = Modifier.align(Alignment.Center),
                onDismiss = onDismiss,
                maxImage = maxImage,
                updateImageType = updateImageType
            )
        }
    }
}

/**
 * Xml 호환용 이미지 피커
 * ```
 * ImagePickerDialogFragment.getInstance(this).show(
 *     parentFragmentManager,ImagePickerDialogFragment.TAG
 * )
 * ```
 */
@AndroidEntryPoint
class ImagePickerDialogFragment : DialogFragment() {

    interface ImageTypeHandler {
        fun receiveImageType(imageType: ImageAddType)
    }

    private var imageTypeHandler: ImageTypeHandler? = null

    fun setHandler(imageTypeHandler: ImageTypeHandler) {
        this.imageTypeHandler = imageTypeHandler
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(android.graphics.Color.TRANSPARENT.toDrawable())
        val maxImage = arguments?.getInt(MAX_IMAGE) ?: 1
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ImagePickerDialogContent(
                    modifier = Modifier,
                    onDismiss = {
                        this@ImagePickerDialogFragment.dismiss()
                    },
                    updateImageType = { imageType ->
                        Log.d("JWH2", imageType.toString())
                        Log.d("JWH2", imageTypeHandler.toString())
                        imageTypeHandler?.receiveImageType(imageType)
                    },
                    maxImage = maxImage
                )
            }
        }
    }

    companion object {
        fun getInstance(
            handler: ImageTypeHandler,
            @IntRange(from = 1) maxImage: Int
        ): ImagePickerDialogFragment {
            return ImagePickerDialogFragment()
                .apply {
                    arguments = Bundle().apply {
                        putInt(MAX_IMAGE, maxImage)
                    }
                    setHandler(handler)
                }
        }

        const val TAG = "ImagePickerDialogFragment"
        const val MAX_IMAGE = "maxImage"
    }

}

@Composable
private fun ImagePickerDialogContent(
    modifier: Modifier,
    onDismiss: () -> Unit,
    @IntRange(from = 1) maxImage: Int = 1,
    updateImageType: (ImageAddType) -> Unit
) {
    val pickLauncher = if (maxImage == 1) {
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                updateImageType(ImageAddType.Content(uri))
                onDismiss()
            }
        }
    } else {
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(maxImage)) { uris ->
            if (uris.isNotEmpty()) {
                updateImageType(ImageAddType.Contents(uris))
                onDismiss()
            }
        }
    }
    Column(
        modifier = Modifier
            .width(310.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .then(modifier),
        verticalArrangement = Arrangement.Center
    ) {
        val textList = listOf(
            "기본 커버 선택",
            "앨범에서 사진 선택"
        )
        val imageRes = listOf(
            R.drawable.icon_pick_default,
            R.drawable.icon_pick_image
        )
        val actionList = listOf(
            {
                updateImageType(ImageAddType.Default)
                onDismiss()
            },
            {
                pickLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )

        repeat(2) {
            val text = textList[it]
            val res = imageRes[it]
            val action = actionList[it]
            Button(
                onClick = action,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = GrayScale8
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(res),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = text,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ImagePickerDialogContentPreview() {
    ImagePickerDialogContent(
        modifier = Modifier, onDismiss = {}, maxImage = 1, updateImageType = {}
    )
}