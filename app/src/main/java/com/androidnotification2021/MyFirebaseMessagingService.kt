package com.androidnotification2021

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FirebaseMessagingService"

    override fun onNewToken(token: String) {
        //monitorar se recebeu ok o token
        Log.d("New_Token", token)
    }

    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "test: ${remoteMessage.from}")

        if (remoteMessage.notification != null) {
            Log.d(TAG, "test: ${remoteMessage.notification?.body}")
            sendNotification(remoteMessage)
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val mBuilder =
            NotificationCompat.Builder(applicationContext, "notify_001")
        val ii = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, ii, 0)

        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(remoteMessage.notification?.body)
        bigText.setBigContentTitle(remoteMessage.notification?.body)
        bigText.setSummaryText(remoteMessage.notification?.body)

        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.drawable.ic_android)
        mBuilder.setContentTitle("Opa")
        mBuilder.setContentText("opa")
        mBuilder.priority = Notification.PRIORITY_MAX
        mBuilder.setStyle(bigText)

        val mNotificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "Your_channel_id"
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }

        mNotificationManager.notify(0, mBuilder.build())
    }
}