package com.example.swift.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.DialogFragment
import com.boombim.android.R

class CongestionDialogFragment(
    private val placeName: String,
    private val onDismiss: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext()).apply {
            setContentView(ComposeView(requireContext()).apply {
                setContent {
                    CongestionSuccessDialog(placeName, onDismiss)
                }
            })
        }
    }
}

@Composable
fun DialogView(placeOfficialName: String){
    val placeName = remember { placeOfficialName }
    Column(
        modifier = Modifier
            .width(315f.dp)
            .clip(RoundedCornerShape(16f.dp))
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = placeName,
            fontSize = 18f.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 30f.dp)
        )
        Text(
            text = "혼잡도알리기",
            fontSize = 18f.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            )
        Image(
            painter = painterResource(id = R.drawable.image_congratuation),
            contentDescription = null,
            Modifier.padding(top = 18f.dp)
        )
        Text(
            text = "지금 바로 지도에서 핀을 확인해보세요!",
            fontSize = 18f.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
        )
        Button(
            onClick = {  },
            modifier = Modifier
                .padding(top = 24f.dp, bottom = 24f.dp, start = 24f.dp, end = 24f.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "확인",
                fontSize = 16f.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
            )
        }
    }

}

@Composable
fun CongestionSuccessDialog(
    placeName: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        DialogView(placeName)
    }
}

@Preview
@Composable
fun DialogContent(){
    DialogView("한국공학대학교")
}