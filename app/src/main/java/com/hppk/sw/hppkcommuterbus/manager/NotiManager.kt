package com.hppk.sw.hppkcommuterbus.manager

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.ui.MainActivity


private const val CHANNEL_ID = "hppk-commuter-bus-noti-channel-id"

object NotiManager {

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(IntentService.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val channelName = context.getString(R.string.hppk_bus_noti_channel_name)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val notiChannel = NotificationChannel(CHANNEL_ID, channelName, importance)
                notificationManager.createNotificationChannel(notiChannel)
            }
        }
    }

    fun notify(context: Context, title: String, text: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notiBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(context)) {
            notify(123, notiBuilder.build())
        }
    }

}