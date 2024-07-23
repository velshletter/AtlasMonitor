package com.example.domain.usecase

import android.util.Log
import com.example.domain.models.TimeItem
import com.example.domain.repository.NotificationSender
import com.example.domain.repository.ServiceManager
import com.example.domain.repository.ServiceStateChecker
import com.example.domain.repository.WebsiteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StartMonitorUseCase(
    private val websiteRepository: WebsiteRepository,
    private val notificationSender: NotificationSender,
    private val serviceManager: ServiceManager,
    private val serviceStateChecker: ServiceStateChecker,
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    fun execute(url: String, timeList: List<TimeItem>) {
        serviceManager.startService()
        coroutineScope.launch {
            while (true) {
                try {
                    val doc = websiteRepository.getWebsite(url)
                    val info =
                        doc.select("div.MuiGrid-grid-md-auto.MuiGrid-item.MuiGrid-root:nth-of-type(3)")
                    for (i in timeList.indices) {
                        if (timeList[i].isSelected) {
                            val sits = info[i].allElements
                            if (sits.size > 1) {
                                notificationSender.sendNotification(timeList[i].time)
                                serviceManager.stopService()
                                return@launch
                            }
                        }
                    }
                    if(!serviceStateChecker.isServiceRunning()){
                        coroutineScope.cancel()
                        break
                    }
                } catch (e: Exception) {
                    Log.d("MyLog", e.message.toString())
                    continue
                } finally {
                    kotlinx.coroutines.delay(5000)
                }
            }
        }
    }
}
