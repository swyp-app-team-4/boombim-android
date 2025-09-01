package com.example.swift

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.boombim.android.R
import com.example.domain.usecase.UpdateFcmToken
import com.example.swift.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var updateFcmToken: UpdateFcmToken

    // 메시지를 수신할 때 호출 된다.
    // 수신된 RemoteMessage 객체를 기준으로 작업을 수행하고 메시지 데이터를 가져올 수 있다.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 메시지에 데이터 페이로드가 포함 되어 있는지 확인한다.
        // 페이로드란 전송된 데이터를 의미한다.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            sendNotification(
                remoteMessage.data["title"].toString(),
                remoteMessage.data["message"].toString()
            )
        } else {
            // 메시지에 알림 페이로드가 포함되어 있는지 확인한다.
            remoteMessage.notification?.let {
                sendNotification(
                    remoteMessage.notification!!.title.toString(),
                    remoteMessage.notification!!.body.toString()
                )
            }
        }
    }

    // 새 토큰이 생성될 때마다 onNewToken 콜백이 호출된다.
    // 등록 토큰이 처음 생성되므로 여기서 토큰을 검색할 수 있다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        CoroutineScope(Dispatchers.IO).launch{
            updateFcmToken(token)
        }
    }

    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icon_btn_alert)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

}