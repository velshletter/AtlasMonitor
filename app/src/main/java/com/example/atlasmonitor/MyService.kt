package com.example.atlasmonitor

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        val notification = NotificationCompat.Builder(this, "channel")
            .setOngoing(true)
            .setSmallIcon(R.drawable.search_128x128)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Идет поиск...")
            .build()
        startForeground(1, notification)
    }
    enum class Actions{
            START, STOP
    }
}