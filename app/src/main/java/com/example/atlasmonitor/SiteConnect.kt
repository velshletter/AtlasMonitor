package com.example.atlasmonitor

import android.Manifest
import android.R
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException

class SiteConnect {

    fun startMonitor(url: String, items: List<TimeItem>, context: Context){
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            while (true) {
                try {
                    val doc = Jsoup.connect(url).get()
                    val info = doc.select("div.MuiGrid-grid-md-auto.MuiGrid-item.MuiGrid-root:nth-of-type(3)")
                    for (i in items.indices) {
                        if (items[i].isSelected) {
                            val sits = info[i].allElements
                            if (sits.size > 1) {
                                sendNot(items[i].time, context)
                                Intent(context, MyService::class.java).also {
                                    it.action = MyService.Actions.STOP.toString()
                                    context.stopService(it)
                                }
                                coroutineScope.cancel()
                            }
                        }
                    }
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
                kotlinx.coroutines.delay(2000)
            }
        }

    }

    private fun sendNot(foundedTime: String, context: Context) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(R.drawable.ic_notification_overlay)
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
//var doc: Document
//lateinit var taskRunnable: Runnable
//taskRunnable = Runnable {
//    try {
//        //doc = Jsoup.connect(url).get()
//        doc = Jsoup.connect("https://atlasbus.by/Маршруты/Минск/Ивье?date=2023-10-03").get()
//        val info = doc.select("div.MuiGrid-grid-md-auto.MuiGrid-item.MuiGrid-root:nth-of-type(3)")
//        for (i in 0..items.size-1) {
//            if (items[i].isSelected) {
//                val sits = info[i].allElements
//                if (sits.size > 1) {
//                    sendNot(items[i].time, context)
//                    Intent(context, MyService::class.java).also {
//                        it.action = MyService.Actions.STOP.toString()
//                        context.stopService(it)
//                    }
//                }
//            }
//        }
//    } catch (e: IOException) {
//        throw RuntimeException(e)
//
//    } finally {
//        handler.postDelayed(taskRunnable, 2000)
//    }
//    //postDelayed(repeatTaskHandler, taskRunnable, "dfs", 5000)
//}
//val thread2 = Thread(taskRunnable)
//thread2.start()
