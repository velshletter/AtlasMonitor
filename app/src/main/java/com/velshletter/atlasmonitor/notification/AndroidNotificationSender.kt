package com.velshletter.atlasmonitor.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.velshletter.atlasmonitor.R
import com.example.domain.repository.NotificationSender

class AndroidNotificationSender(private val context: Context) : NotificationSender {
    override fun sendNotification(foundedTime: String) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(R.drawable.search_128x128)
            .setContentTitle("AtlasMonitor")
            .setContentText("Найдено место на время $foundedTime")
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManagerCompat.notify(0, builder.build())
    }
}