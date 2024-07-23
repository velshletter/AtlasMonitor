package com.velshletter.atlasmonitor.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.velshletter.atlasmonitor.R
import com.example.data.repository.ServiceStateCheckerImpl
import com.example.domain.repository.ServiceStateChecker
import com.velshletter.atlasmonitor.presentation.MainActivity

class ForegroundService : Service() {

    private lateinit var serviceStateChecker: ServiceStateChecker

    override fun onCreate() {
        super.onCreate()
        serviceStateChecker = ServiceStateCheckerImpl(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceStateChecker.upDateServiceState(isRunning = true)
        start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceStateChecker.upDateServiceState(isRunning = false)
    }

    @SuppressLint("ForegroundServiceType")
    private fun start() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarm = "Идет поиск..."
        val notification = NotificationCompat.Builder(this, "channel")
            .setOngoing(true)
            .setSmallIcon(R.drawable.search_128x128)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(alarm)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}